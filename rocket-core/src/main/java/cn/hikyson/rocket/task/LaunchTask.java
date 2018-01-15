package cn.hikyson.rocket.task;

import android.os.Process;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

import cn.hikyson.rocket.util.Execs;

/**
 * Created by kysonchao on 2017/12/26.
 */
@Keep
public abstract class LaunchTask implements Task, Condition, Dependecy, Priority, TaskCallback {
    //task依赖其他task的条件锁
    private CountDownLatch mDependencyLatch;
    //task被其他模块依赖的条件锁
    private CountDownLatch mBedependencyLatch;

    public LaunchTask() {
        mDependencyLatch = new CountDownLatch(dependsOn().size());
        mBedependencyLatch = new CountDownLatch(1);
    }

    /**
     * 等待前置条件全部满足之后执行
     *
     * @throws InterruptedException
     */
    @Override
    public final void waitMetCondition() throws InterruptedException {
        mDependencyLatch.await();
    }

    /**
     * 前置条件准备
     */
    @Override
    public final void conditionPrepare() {
        mDependencyLatch.countDown();
    }

    /**
     * 剩余多少条件可以满足run的要求
     *
     * @return
     */
    @Override
    public final long conditionLeft() {
        return mDependencyLatch.getCount();
    }

    @Override
    public final void dependencyUnlock() {
        mBedependencyLatch.countDown();
    }

    @Override
    public final void dependencyWait() throws InterruptedException {
        mBedependencyLatch.await();
    }

    /**
     * 当前任务是否已经完成
     *
     * @return
     */
    @Override
    public boolean isDone() {
        return mBedependencyLatch.getCount() == 0;
    }

    @NonNull
    @Override
    public String taskName() {
        return getClass().getSimpleName();
    }

    @Override
    public int priority() {
        return Process.THREAD_PRIORITY_BACKGROUND + Process.THREAD_PRIORITY_MORE_FAVORABLE;
    }

    @NonNull
    @Override
    public Executor runOn() {
        return Execs.launchExecutor();
    }

    @Override
    public void beforeWait() {
    }

    @Override
    public void beforeRun() {
    }

    @Override
    public void onTaskDone(TaskRecord taskRecord) {
    }

    @Override
    public String toString() {
        return "ConditionTask{" +
                "taskName=" + taskName() +
                ", priority=" + priority() +
                ", runOn=" + runOn().getClass().getSimpleName() +
                ", depends=" + dependsOn() +
                ", conditionLeft=" + conditionLeft() +
                '}';
    }
}
