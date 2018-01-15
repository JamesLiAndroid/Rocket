package cn.hikyson.rocket.monitor;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;

import java.util.ArrayList;
import java.util.List;

import cn.hikyson.rocket.callback.ITimeoutHandler;
import cn.hikyson.rocket.task.LaunchTask;

/**
 * 任务监视器
 * Created by kysonchao on 2018/1/9.
 */
public class TaskDelayChecker {
    /**
     * 延时确认任务是否已经完成
     *
     * @param tasks
     * @param delayTimeMillis
     * @param iTimeoutHandler
     */
    public static void delayCheckTaskAlive(final List<LaunchTask> tasks, long delayTimeMillis, final ITimeoutHandler iTimeoutHandler) {
        if (delayTimeMillis <= 0 || iTimeoutHandler == null) {
            return;
        }
        final HandlerThread handlerThread = new HandlerThread("rocket-task-timeout-checker", Process.THREAD_PRIORITY_BACKGROUND + Process.THREAD_PRIORITY_MORE_FAVORABLE);
        handlerThread.start();
        new Handler(handlerThread.getLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                List<LaunchTask> undoneTasks = new ArrayList<>();
                //如果当前还有任务没有完成的话，输出异常
                int size = tasks.size();
                for (int i = 0; i < size; i++) {
                    final LaunchTask task = tasks.get(i);
                    if (!task.isDone()) {
                        undoneTasks.add(task);
                    }
                }
                if (!undoneTasks.isEmpty()) {
                    iTimeoutHandler.onTimeout(undoneTasks);
                }
                handlerThread.quit();
            }
        }, delayTimeMillis);
    }
}
