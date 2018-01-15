package cn.hikyson.rocket.callback;

import android.support.annotation.WorkerThread;

import java.util.List;

import cn.hikyson.rocket.task.LaunchTask;

/**
 * Created by kysonchao on 2018/1/9.
 */
public interface ITimeoutHandler {
    @WorkerThread
    void onTimeout(List<LaunchTask> timeoutTasks);
}
