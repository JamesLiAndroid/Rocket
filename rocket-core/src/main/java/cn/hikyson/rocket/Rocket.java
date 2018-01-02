package cn.hikyson.rocket;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import cn.hikyson.rocket.parser.TaskParser;
import cn.hikyson.rocket.task.ConditionTask;
import cn.hikyson.rocket.task.TailTask;
import cn.hikyson.rocket.task.TaskScheduer;
import cn.hikyson.rocket.util.L;

/**
 * 入口类
 * Created by kysonchao on 2017/12/26.
 */
public class Rocket {

    private List<ConditionTask> mConditionTasks;

    public synchronized Rocket from(Context context, String assetFile) throws Throwable {
        return from(TaskParser.parse(context, assetFile));
    }

    public synchronized Rocket from(final List<ConditionTask> conditionTasks) {
        mConditionTasks = new ArrayList<>(conditionTasks);
        TailTask tailTask = new TailTask() {
            @Override
            public List<String> dependsOn() {
                return parseTaskNames(conditionTasks);
            }
        };
        mConditionTasks.add(tailTask);
        L.d("原始任务列表: " + String.valueOf(mConditionTasks));
        return this;
    }

    public synchronized void launch() {
        new TaskScheduer(mConditionTasks).schedule();
    }

    private synchronized List<String> parseTaskNames(List<ConditionTask> conditionTasks) {
        List<String> taskNames = new ArrayList<>();
        for (ConditionTask task : conditionTasks) {
            taskNames.add(task.taskName());
        }
        return taskNames;
    }
}
