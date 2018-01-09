package cn.hikyson.rocket.exception;

import cn.hikyson.rocket.task.LaunchTask;

/**
 * Created by kysonchao on 2018/1/9.
 */
public interface IErrorHandler {
    void onError(LaunchTask task, Throwable e);
}
