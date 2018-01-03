package cn.hikyson.rocket.task;

/**
 * Created by kysonchao on 2017/12/28.
 */
public abstract class TestConditionTask extends ConditionTask {
    private cn.hikyson.rocket.schdule.TaskCallback mTaskCallback;
    private long mRunTime;


    public TestConditionTask(cn.hikyson.rocket.schdule.TaskCallback taskCallback, long runTime) {
        mTaskCallback = taskCallback;
        mRunTime = runTime;
    }

    @Override
    public void run() throws Throwable {
        Thread.sleep(mRunTime);
    }
}
