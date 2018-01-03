package cn.hikyson.rocket.schdule;

import android.os.SystemClock;

import java.util.ArrayList;
import java.util.List;

import cn.hikyson.rocket.task.ConditionTask;
import cn.hikyson.rocket.util.L;

/**
 * Created by kysonchao on 2017/12/27.
 */
public class TaskScheduer {
    private List<ConditionTask> mTasks;

    public TaskScheduer(List<ConditionTask> originTasks) {
        mTasks = topologicalSort(originTasks);
        L.d("解析任务依赖关系，" + String.valueOf(mTasks));
    }

    public void schedule() {
        for (final ConditionTask conditionTask : mTasks) {
            conditionTask.runOn().execute(new Runnable() {
                @Override
                public void run() {
                    L.d(conditionTask.taskName() + " 还有" + conditionTask.conditionLeft() + "前置条件");
                    L.d(conditionTask.taskName() + " 等待条件允许...");
                    Thread.currentThread().setPriority(conditionTask.priority());
                    long waitTime = System.currentTimeMillis();
                    long waitThreadTime = SystemClock.currentThreadTimeMillis();
                    try {
                        conditionTask.waitMetCondition();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    long runTime = System.currentTimeMillis();
                    long runThreadTime = SystemClock.currentThreadTimeMillis();
                    L.d(conditionTask.taskName() + " 条件满足，正在run...");
                    try {
                        conditionTask.run();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                    conditionTask.onDone();
                    long doneTime = System.currentTimeMillis();
                    long doneThreadTime = SystemClock.currentThreadTimeMillis();
                    L.d(conditionTask.taskName() + " run done! " + String.valueOf(new TaskTimeRecord(waitTime, waitThreadTime, runTime, runThreadTime, doneTime, doneThreadTime)));
                    prepareForChildren(conditionTask);
                }
            });
        }
    }

    /**
     * 很多任务依赖当前任务，为这些任务做准备
     *
     * @param selfTask
     */
    private void prepareForChildren(ConditionTask selfTask) {
        for (ConditionTask other : mTasks) {
            if (other.dependsOn().contains(selfTask.taskName())) {
                other.conditionPrepare();
                L.d(selfTask.taskName() + " 为 " + other.taskName() + " 执行countDown ， " + other.taskName() + "距离能够执行还剩" + other.conditionLeft());
            }
        }
    }

    /**
     * 任务的有向无环图的拓扑排序
     *
     * @return
     */
    private synchronized List<ConditionTask> topologicalSort(List<ConditionTask> originTasks) {
        Graph graph = new Graph(originTasks.size());
        for (int i = 0; i < originTasks.size(); i++) {
            ConditionTask self = originTasks.get(i);
            for (String taskName : self.dependsOn()) {
                int indexOfDepend = getIndexOfTask(originTasks, taskName);
                if (indexOfDepend < 0) {
                    throw new IllegalStateException(self.taskName() + "depends on " + taskName + " can not be found in task list");
                }
                graph.addEdge(indexOfDepend, i);
            }
        }
        List<Integer> indexOrder = graph.topologicalSort();
        List<ConditionTask> allTasksSorted = new ArrayList<>();
        for (int i : indexOrder) {
            allTasksSorted.add(originTasks.get(i));
        }
        return allTasksSorted;
    }

    /**
     * 获取任务在任务列表中的index
     *
     * @param originTasks
     * @param taskName
     * @return
     */
    private int getIndexOfTask(List<ConditionTask> originTasks, String taskName) {
        final int size = originTasks.size();
        for (int i = 0; i < size; i++) {
            if (taskName.equals(originTasks.get(i).taskName())) {
                return i;
            }
        }
        return -1;
    }

    private static class TaskTimeRecord {
        private long waitDuration;
        private long waitThreadDuration;
        private long runDuration;
        private long runThreadDuration;

        public TaskTimeRecord(long waitTime, long waitThreadTime, long runTime, long runThreadTime, long doneTime, long doneThreadTime) {
            waitDuration = runTime - waitTime;
            waitThreadDuration = runThreadTime - waitThreadTime;
            runDuration = doneTime - runTime;
            runThreadDuration = doneThreadTime - runThreadTime;
        }

        @Override
        public String toString() {
            return "TaskTimeRecord{" +
                    "waitDuration=" + waitDuration +
                    "ms, waitThreadDuration=" + waitThreadDuration +
                    "ms, runDuration=" + runDuration +
                    "ms, runThreadDuration=" + runThreadDuration +
                    "ms}";
        }
    }

}
