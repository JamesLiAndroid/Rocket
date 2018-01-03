//package cn.hikyson.rocket.helper;
//
//import android.support.annotation.NonNull;
//
//import java.util.concurrent.ThreadFactory;
//import java.util.concurrent.atomic.AtomicInteger;
//
//import cn.hikyson.rocket.task.PriorityRunnable;
//
///**
// * Created by kysonchao on 2018/1/3.
// */
//public class DefaultPriorityThreadFactory implements ThreadFactory {
//    private static final AtomicInteger poolNumber = new AtomicInteger(1);
//    private final AtomicInteger threadNumber = new AtomicInteger(1);
//    private final ThreadGroup group;
//    private final String namePrefix;
//
//    public DefaultPriorityThreadFactory() {
//        SecurityManager s = System.getSecurityManager();
//        group = (s != null) ? s.getThreadGroup() :
//                Thread.currentThread().getThreadGroup();
//        namePrefix = "rocket-pool-" +
//                poolNumber.getAndIncrement() +
//                "-thread-";
//    }
//
//    public Thread newThread(@NonNull Runnable r) {
//        Thread t = new Thread(group, r,
//                namePrefix + threadNumber.getAndIncrement(),
//                0);
//        if (t.isDaemon()) {
//            t.setDaemon(false);
//        }
//        //只允许提交PriorityRunnable
//        PriorityRunnable priorityRunnable = (PriorityRunnable) r;
//        t.setPriority(priorityRunnable.priority());
//        return t;
//    }
//}
