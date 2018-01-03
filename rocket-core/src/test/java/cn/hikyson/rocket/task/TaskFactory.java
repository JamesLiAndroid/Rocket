package cn.hikyson.rocket.task;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * Created by kysonchao on 2017/12/28.
 */
public class TaskFactory {
    public static ConditionTask create(final String taskName, long runTime, final List<String> depends, final Executor executor, cn.hikyson.rocket.schdule.TaskCallback taskCallback) {
        return new TestConditionTask(taskCallback, runTime) {
            @NonNull
            @Override
            public String taskName() {
                return taskName;
            }

            @NonNull
            @Override
            public List<String> dependsOn() {
                return depends;
            }

            @NonNull
            @Override
            public Executor runOn() {
                return executor;
            }
        };
    }

}
