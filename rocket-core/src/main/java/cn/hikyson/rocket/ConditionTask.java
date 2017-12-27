package cn.hikyson.rocket;

import java.util.concurrent.CountDownLatch;

/**
 * Created by kysonchao on 2017/12/26.
 */
public abstract class ConditionTask implements Task, Condition {
    private CountDownLatch mCountDownLatch;

    public ConditionTask() {
        mCountDownLatch = new CountDownLatch(dependsOn().size());
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
        return taskName();
    }
}
