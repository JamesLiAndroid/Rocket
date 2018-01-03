package cn.hikyson.rocket.task;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.Executor;

/**
 * Created by kysonchao on 2018/1/3.
 */
public interface ITailTask {
    @Nullable
    String taskName();

    @NonNull
    Executor runOn();
}
