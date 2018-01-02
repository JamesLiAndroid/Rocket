package cn.hikyson.rocket.task;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;

import java.util.concurrent.CountDownLatch;

/**
 * Created by kysonchao on 2017/12/26.
 */
@Keep
public abstract class ConditionTask implements Task, Condition {
    private CountDownLatch mCountDownLatch;

    public ConditionTask() {
        mCountDownLatch = new CountDownLatch(dependsOn().size());
    }

    @NonNull
    @Override
    public String taskName() {
        return getClass().getSimpleName();
    }

    /**
     * 等待前置条件全部满足之后执行
     *
     * @throws InterruptedException
     */
    @Override
    public void waitMetCondition() throws InterruptedException {
        mCountDownLatch.await();
    }

    /**
     * 前置条件准备
     */
    @Override
    public void conditionPrepare() {
        mCountDownLatch.countDown();
    }

    /**
     * 剩余多少条件可以满足run的要求
     *
     * @return
     */
    @Override
    public long conditionLeft() {
        return mCountDownLatch.getCount();
    }

    @Override
    public String toString() {
        return "ConditionTask{" +
                "taskName=" + taskName() +
                " , depends=" + dependsOn() +
                " , runOn=" + runOn().getClass().getSimpleName() +
                " , conditionLeft=" + conditionLeft() +
                '}';
    }
}
