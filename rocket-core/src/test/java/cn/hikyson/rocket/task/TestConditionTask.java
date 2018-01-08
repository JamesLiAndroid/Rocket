package cn.hikyson.rocket.task;

/**
 * Created by kysonchao on 2017/12/28.
 */
public abstract class TestConditionTask extends LaunchTask {
    private long mRunTime;


    public TestConditionTask(long runTime) {
        mRunTime = runTime;
    }

    @Override
    public void run() throws Throwable {
        Thread.sleep(mRunTime);
    }
}
