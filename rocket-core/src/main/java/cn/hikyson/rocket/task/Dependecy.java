package cn.hikyson.rocket.task;

/**
 * Created by kysonchao on 2018/1/2.
 */
public interface Dependecy {
    void dependencyUnlock();

    void dependencyWait() throws InterruptedException;

    boolean isDone();
}
