package cn.hikyson.rocket;

import android.support.annotation.NonNull;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import cn.hikyson.rocket.callback.IErrorHandler;
import cn.hikyson.rocket.callback.ITimeoutHandler;
import cn.hikyson.rocket.task.LaunchTask;
import cn.hikyson.rocket.util.Execs;

/**
 * Created by kysonchao on 2018/1/9.
 */
@RunWith(RobolectricTestRunner.class)
@Config(application = RocketApp.class, shadows = {TestRocketLooper.class})
public class RocketTest {
    @Test
    public void testTimeout() throws Exception {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        LaunchTask launchTask = new LaunchTask() {
            @Override
            public void run() throws Throwable {
                System.out.println("launchTask running...");
                Thread.sleep(3000);
                System.out.println("launchTask end.");
            }

            @NonNull
            @Override
            public List<String> dependsOn() {
                return Collections.emptyList();
            }

            @NonNull
            @Override
            public Executor runOn() {
                return Execs.io();
            }
        };
        Rocket.get().from(RuntimeEnvironment.application, Collections.singletonList(launchTask)).errorHandler(new IErrorHandler() {
            @Override
            public void onError(LaunchTask task, Throwable e) {

            }
        }).timeoutHandler(2000, new ITimeoutHandler() {
            @Override
            public void onTimeout(List<LaunchTask> timeoutTasks) {
                System.out.println("onTimeout");
                countDownLatch.countDown();
            }
        }).launch();
//        Assert.assertTrue(countDownLatch.await(5, TimeUnit.SECONDS));
    }


    @Test
    public void testError() throws Exception {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        LaunchTask launchTask = new LaunchTask() {
            @Override
            public void run() throws Throwable {
                Thread.sleep(2000);
                throw new IllegalStateException("this is a error");
            }

            @NonNull
            @Override
            public List<String> dependsOn() {
                return Collections.emptyList();
            }

            @NonNull
            @Override
            public Executor runOn() {
                return Execs.io();
            }
        };
        Rocket.get().from(RuntimeEnvironment.application, Collections.singletonList(launchTask)).errorHandler(new IErrorHandler() {
            @Override
            public void onError(LaunchTask task, Throwable e) {
                System.out.println(task.taskName() + String.valueOf(e));
                countDownLatch.countDown();
            }
        }).timeoutHandler(10000, new ITimeoutHandler() {
            @Override
            public void onTimeout(List<LaunchTask> timeoutTasks) {
            }
        }).launch();
        Assert.assertTrue(countDownLatch.await(5000, TimeUnit.SECONDS));
    }
}