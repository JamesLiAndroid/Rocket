package cn.hikyson.rocket.util;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by kysonchao on 2018/1/2.
 */
public class Execs {
    private static Executor sMain;
    private static ExecutorService sIo;
    private static ThreadPoolExecutor sLaunchExecutor;

    /**
     * 根据cpu个数决定的线程数量
     * 所有线程包括核心线程也会销毁
     * 线程空闲生存时间为5秒
     *
     * @return
     */
    public static Executor launchExecutor() {
        if (sLaunchExecutor == null || sLaunchExecutor.isShutdown()) {
            int cpuCount = Runtime.getRuntime().availableProcessors();
            int corePoolSize = cpuCount + 1;
            sLaunchExecutor = new ThreadPoolExecutor(corePoolSize, corePoolSize, 5L, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>(), new RocketThreadFactory());
            sLaunchExecutor.allowCoreThreadTimeOut(true);
        }
        return sLaunchExecutor;
    }

    public static Executor io() {
        if (sIo == null || sIo.isShutdown()) {
            sIo = Executors.newCachedThreadPool(new RocketThreadFactory());
        }
        return sIo;
    }

    public static Executor main() {
        if (sMain == null) {
            sMain = new MainThreadExecutor();
        }
        return sMain;
    }

    private static class MainThreadExecutor implements Executor {

        private Handler mUiHandler;

        MainThreadExecutor() {
            mUiHandler = new Handler(Looper.getMainLooper());
        }

        @Override
        public void execute(@NonNull Runnable command) {
            mUiHandler.post(command);
        }
    }

    private static class RocketThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        RocketThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = "rocket-pool-" + poolNumber.getAndIncrement() + "-thread-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }


}
