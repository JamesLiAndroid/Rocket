package cn.hikyson.rocket;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import cn.hikyson.rocket.parser.TaskParser;
import cn.hikyson.rocket.task.ConditionTask;
import cn.hikyson.rocket.task.TailTask;
import cn.hikyson.rocket.task.TaskCallback;
import cn.hikyson.rocket.task.TaskScheduer;
import cn.hikyson.rocket.util.Execs;
import cn.hikyson.rocket.util.L;

/**
 * 入口类
 * Created by kysonchao on 2017/12/26.
 */
public class Rocket {

    private List<ConditionTask> mConditionTasks;

    private Executor mIoExec;

    public synchronized Rocket from(Context context, String assetFile) throws Throwable {
        return from(TaskParser.parse(context, assetFile));
    }

    public synchronized Rocket from(final List<ConditionTask> conditionTasks) {
        mConditionTasks = new ArrayList<>(conditionTasks);
        TailTask tailTask = new TailTask() {
            @Override
            public List<String> dependsOn() {
                //如果用户任务依赖尾部任务，那么尾部任务就不依赖该用户
                return parseTaskNames(conditionTasks, new TaskFilter() {
                    @Override
                    public boolean filter(ConditionTask task) {
                        return !task.dependsOn().contains(TailTask.class.getSimpleName());
                    }
                });
            }

            @Override
            public Executor runOn() {
                if (mIoExec == null) {
                    return Execs.io();
                }
                return mIoExec;
            }
        };
        mConditionTasks.add(tailTask);
        L.d("原始任务列表: " + String.valueOf(mConditionTasks));
        return this;
    }

    public synchronized void launch() {
        new TaskScheduer(mConditionTasks).schedule();
    }

    public void listen(String taskName, TaskCallback taskCallback) {

    }

    public Rocket io(Executor exec) {
        mIoExec = exec;
        return this;
    }

    private interface TaskFilter {
        boolean filter(ConditionTask task);
    }

    private synchronized List<String> parseTaskNames(List<ConditionTask> conditionTasks, TaskFilter taskFilter) {
        List<String> taskNames = new ArrayList<>();
        for (ConditionTask task : conditionTasks) {
            if (taskFilter == null || taskFilter.filter(task)) {
                taskNames.add(task.taskName());
            }
        }
        return taskNames;
    }
}
