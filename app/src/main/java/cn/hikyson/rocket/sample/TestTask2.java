package cn.hikyson.rocket.sample;

import android.os.Process;
import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

import cn.hikyson.rocket.task.LaunchTask;
import cn.hikyson.rocket.util.*;

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
        Thread.sleep(5000);
    }

    @Override
    public int priority() {
        return super.priority() + Process.THREAD_PRIORITY_MORE_FAVORABLE;
    }

    @NonNull
    @Override
    public List<String> dependsOn() {
        return Collections.singletonList("test1");
    }

    @NonNull
    @Override
    public Executor runOn() {
        return cn.hikyson.rocket.util.Execs.main();
    }
}
