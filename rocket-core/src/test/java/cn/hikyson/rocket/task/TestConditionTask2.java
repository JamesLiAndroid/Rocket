package cn.hikyson.rocket.task;

/**
 * Created by kysonchao on 2017/12/28.
 */
public abstract class TestConditionTask2 extends LaunchTask {

    public TestConditionTask2() {
    }

    @Override
    public void run() throws Throwable {
        for (int i = 0; i < 10000; i++) {
            int t = i + 1;
            Thread.sleep(1);
        }
    }
}
