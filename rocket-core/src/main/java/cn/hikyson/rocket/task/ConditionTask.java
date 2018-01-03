package cn.hikyson.rocket.task;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;

import java.util.concurrent.CountDownLatch;

/**
 * Created by kysonchao on 2017/12/26.
 */
@Keep
public abstract class ConditionTask implements Task, Condition, Dependecy, Priority {
    //task依赖其他task的条件锁
    private CountDownLatch mDependencyLatch;
    //task被其他模块依赖的条件锁
    private CountDownLatch mBedependencyLatch;

    public ConditionTask() {
        mDependencyLatch = new CountDownLatch(dependsOn().size());
        mBedependencyLatch = new CountDownLatch(1);
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
        mDependencyLatch.await();
    }

    /**
     * 前置条件准备
     */
    @Override
    public void conditionPrepare() {
        mDependencyLatch.countDown();
    }

    /**
     * 剩余多少条件可以满足run的要求
     *
     * @return
     */
    @Override
    public long conditionLeft() {
        return mDependencyLatch.getCount();
    }

    @Override
    public void onDone() {
        mBedependencyLatch.countDown();
    }

    @Override
    public void waitDone() throws InterruptedException {
        mBedependencyLatch.await();
    }

    @Override
    public int priority() {
        return Thread.NORM_PRIORITY;
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
