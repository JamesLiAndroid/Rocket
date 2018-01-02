package cn.hikyson.rocket.util;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

/**
 * Created by kysonchao on 2018/1/2.
 */
public class MainThreadExecutor implements Executor {

    private Handler mUiHandler;

    public MainThreadExecutor() {
        mUiHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void execute(@NonNull Runnable command) {
        mUiHandler.post(command);
    }
}
