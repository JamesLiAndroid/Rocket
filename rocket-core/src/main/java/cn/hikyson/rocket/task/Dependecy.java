package cn.hikyson.rocket.task;

/**
 * Created by kysonchao on 2018/1/2.
 */
public interface Dependecy {
    void onDone();

    void waitDone() throws InterruptedException;
}
