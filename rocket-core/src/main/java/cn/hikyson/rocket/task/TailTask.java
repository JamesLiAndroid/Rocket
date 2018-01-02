package cn.hikyson.rocket.task;

import java.util.concurrent.Executor;

import cn.hikyson.rocket.util.Execs;

/**
 * Created by kysonchao on 2018/1/2.
 */
public abstract class TailTask extends ConditionTask {

    @Override
    public String taskName() {
        return getClass().getSimpleName();
    }

    @Override
    public void run() throws Throwable {
        //do nothing
    }

    @Override
    public Executor runOn() {
        return Execs.MAIN_EXECUTOR;
    }
}
