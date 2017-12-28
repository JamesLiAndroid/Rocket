package cn.hikyson.rocket.task;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Description:
 * 线程同步锁
 * 作用：保证{@link SimpleLock#waitReady()}之后的操作在{@link SimpleLock#execute(Runnable)}完成后才执行
 * Author: Kyson
 * Date: 2017/1/18
 */
public class SimpleLock {
    private AtomicBoolean mIsReady = new AtomicBoolean(false);
    private final Object mLock = new Object();

    /**
     * 执行任务
     *
     * @param runnable
     * @return
     */
    public void execute(final Runnable runnable) {
        mIsReady.set(false);
        runnable.run();
        synchronized (mLock) {
            mIsReady.set(true);
            mLock.notifyAll();
        }
    }

    /**
     * 等待完成
     */
    public void waitReady() {
        synchronized (mLock) {
            if (!mIsReady.get()) {
                synchronized (mLock) {
                    try {
                        mLock.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }
}
