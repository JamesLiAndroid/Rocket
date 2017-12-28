package cn.hikyson.rocket.task;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * Created by kysonchao on 2017/12/28.
 */
public class TaskFactory {
    public static ConditionTask create(final String taskName, long runTime, final List<String> depends, final Executor executor, TaskCallback taskCallback) {
        return new TestConditionTask(taskCallback, runTime) {
            @Override
            public String taskName() {
                return taskName;
            }

            @Override
            public List<String> dependsOn() {
                return depends;
            }

            @Override
            public Executor runOn() {
                return executor;
            }
        };
    }

}
