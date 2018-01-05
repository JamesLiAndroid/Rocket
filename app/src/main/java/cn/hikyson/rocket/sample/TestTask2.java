package cn.hikyson.rocket.sample;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

import cn.hikyson.rocket.task.LaunchTask;

/**
 * Created by kysonchao on 2017/12/28.
 */
public class TestTask2 extends LaunchTask {
    @NonNull
    @Override
    public String taskName() {
        return "test2";
    }

    @Override
    public void run() throws Throwable {
        Thread.sleep(3000);
    }

    @NonNull
    @Override
    public List<String> dependsOn() {
        return Collections.singletonList("test1");
    }

    @NonNull
    @Override
    public Executor runOn() {
        return Execs.io;
    }
}
