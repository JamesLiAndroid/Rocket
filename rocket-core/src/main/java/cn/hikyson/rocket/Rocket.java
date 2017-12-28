package cn.hikyson.rocket;

import android.content.Context;

import java.util.List;

import cn.hikyson.rocket.parser.TaskParser;
import cn.hikyson.rocket.task.ConditionTask;
import cn.hikyson.rocket.task.TaskScheduer;
import cn.hikyson.rocket.util.L;

/**
 * 入口类
 * Created by kysonchao on 2017/12/26.
 */
public class Rocket {

    private List<ConditionTask> mConditionTasks;

    public Rocket from(Context context, String assetFile) throws Throwable {
        mConditionTasks = TaskParser.parse(context, assetFile);
        L.d("原始任务列表: " + String.valueOf(mConditionTasks));
        return this;
    }

    public Rocket from(List<ConditionTask> conditionTasks) {
        mConditionTasks = conditionTasks;
        L.d("原始任务列表: " + String.valueOf(mConditionTasks));
        return this;
    }

    public void launch() {
        new TaskScheduer(mConditionTasks).schedule();
    }
}
