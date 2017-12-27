package cn.hikyson.rocket;

/**
 * Created by kysonchao on 2017/12/27.
 */
public interface Condition {
    void waitMetCondition() throws InterruptedException;

    void conditionPrepare();

    long conditionLeft();
}
