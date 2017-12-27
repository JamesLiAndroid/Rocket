package cn.hikyson.rocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kysonchao on 2017/12/27.
 */
public class TaskScheduer {
    private List<ConditionTask> mTasks;

    public TaskScheduer(List<ConditionTask> originTasks) {
        mTasks = topologicalSort(originTasks);
        L.d(String.valueOf(mTasks));
    }

    /**
     * 任务的有向无环图的拓扑排序
     *
     * @return
     */
    private List<ConditionTask> topologicalSort(List<ConditionTask> originTasks) {
        Map<String, ConditionTask> conditionTaskNameMap = new HashMap<>();
        for (int i = 0; i < originTasks.size(); i++) {
            ConditionTask task = originTasks.get(i);
            conditionTaskNameMap.put(task.taskName(), task);
        }
        Graph graph = new Graph(originTasks.size());
        for (int i = 0; i < originTasks.size(); i++) {
            ConditionTask self = originTasks.get(i);
            for (String taskName : self.dependsOn()) {
                int indexOfDepend = originTasks.indexOf(conditionTaskNameMap.get(taskName));
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

    public void schedule() {
        for (final ConditionTask conditionTask : mTasks) {
            conditionTask.runOn().execute(new Runnable() {
                @Override
                public void run() {
                    L.d(conditionTask.taskName() + " 共有" + conditionTask.conditionLeft() + "前置条件");
                    L.d(conditionTask.taskName() + " 等待条件允许...");
                    try {
                        conditionTask.waitMetCondition();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    L.d(conditionTask.taskName() + " 条件满足，正在run...");
                    try {
                        conditionTask.run();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                    L.d(conditionTask.taskName() + " run 完了.");
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
}
