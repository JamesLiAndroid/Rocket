package cn.hikyson.rocket.task;

import android.os.Process;
import android.os.SystemClock;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import cn.hikyson.rocket.callback.IErrorHandler;
import cn.hikyson.rocket.callback.ITasksFinishCallback;
import cn.hikyson.rocket.callback.ITimeoutHandler;
import cn.hikyson.rocket.monitor.TaskDelayChecker;
import cn.hikyson.rocket.util.L;

/**
 * Created by kysonchao on 2017/12/27.
 */
public class TaskScheduer {
    private List<LaunchTask> mTasks;

    public TaskScheduer(List<LaunchTask> originTasks) {
        mTasks = topologicalSort(originTasks);
        L.d("Parsed task dependencies: " + String.valueOf(mTasks));
    }

    public void schedule(final IErrorHandler iErrorHandler, long timeout, ITimeoutHandler iTimeoutHandler, final ITasksFinishCallback iTasksFinishCallback) {
        final AtomicInteger taskDoneCounter = new AtomicInteger(0);
        final int taskCount = mTasks.size();
        for (final LaunchTask task : mTasks) {
            task.runOn().execute(new Worker(iErrorHandler, task, mTasks, new Worker.TaskDoneCallback() {
                @Override
                public void onTaskDone() {
                    if (taskDoneCounter.addAndGet(1) == taskCount && iTasksFinishCallback != null) {
                        iTasksFinishCallback.onTasksFinished();
                    }
                }
            }));
        }
        TaskDelayChecker.delayCheckTaskAlive(mTasks, timeout, iTimeoutHandler);
    }

    /**
     * 真正承载任务的worker
     */
    private static class Worker implements Runnable {
        private IErrorHandler iErrorHandler;
        private LaunchTask task;
        private List<LaunchTask> mTasks;
        private TaskDoneCallback mTaskDoneCallback;

        Worker(IErrorHandler iErrorHandler, LaunchTask task, List<LaunchTask> tasks, @NonNull TaskDoneCallback taskDoneCallback) {
            this.iErrorHandler = iErrorHandler;
            this.mTaskDoneCallback = taskDoneCallback;
            this.task = task;
            this.mTasks = tasks;
        }

        @Override
        public void run() {
            Process.setThreadPriority(task.priority());
            L.d(task.taskName() + " waiting " + task.conditionLeft() + " conditions...");
            long waitTime = System.currentTimeMillis();
            task.beforeWait();
            try {
                task.waitMetCondition();
            } catch (InterruptedException e) {
                L.e(String.valueOf(e));
                if (iErrorHandler != null) {
                    iErrorHandler.onError(task, e);
                }
            }
            L.d(task.taskName() + " conditions met, running...");
            long runTime = System.currentTimeMillis();
            long runThreadTime = SystemClock.currentThreadTimeMillis();
            task.beforeRun();
            try {
                task.run();
            } catch (Throwable e) {
                L.e(String.valueOf(e));
                if (iErrorHandler != null) {
                    iErrorHandler.onError(task, e);
                }
            }
            task.dependencyUnlock();
            long doneTime = System.currentTimeMillis();
            long doneThreadTime = SystemClock.currentThreadTimeMillis();
            TaskRecord taskRecord = new TaskRecord(waitTime, runTime, runThreadTime, doneTime, doneThreadTime);
            L.d(task.taskName() + " run done! " + String.valueOf(taskRecord));
            task.onTaskDone(taskRecord);
            prepareForChildren(task);
            mTaskDoneCallback.onTaskDone();
        }

        /**
         * 很多任务依赖当前任务，为这些任务做准备
         *
         * @param selfTask 当前任务
         */
        private void prepareForChildren(LaunchTask selfTask) {
            for (LaunchTask other : mTasks) {
                if (other.dependsOn().contains(selfTask.taskName())) {
                    other.conditionPrepare();
                    L.d(selfTask.taskName() + " countdown for " + other.taskName() + ", who has " + other.conditionLeft() + " condition left");
                }
            }
        }

        private interface TaskDoneCallback {
            void onTaskDone();
        }
    }

    /**
     * 任务的有向无环图的拓扑排序
     *
     * @return
     */
    private synchronized List<LaunchTask> topologicalSort(List<LaunchTask> originTasks) {
        Graph graph = new Graph(originTasks.size());
        for (int i = 0; i < originTasks.size(); i++) {
            LaunchTask self = originTasks.get(i);
            for (String taskName : self.dependsOn()) {
                int indexOfDepend = getIndexOfTask(originTasks, taskName);
                if (indexOfDepend < 0) {
                    throw new IllegalStateException(self.taskName() + " depends on " + taskName + " can not be found in task list");
                }
                graph.addEdge(indexOfDepend, i);
            }
        }
        List<Integer> indexOrder = graph.topologicalSort();
        List<LaunchTask> allTasksSorted = new ArrayList<>();
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
    private int getIndexOfTask(List<LaunchTask> originTasks, String taskName) {
        final int size = originTasks.size();
        for (int i = 0; i < size; i++) {
            if (taskName.equals(originTasks.get(i).taskName())) {
                return i;
            }
        }
        return -1;
    }
}
