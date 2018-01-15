package cn.hikyson.rocket.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.Executor;

import cn.hikyson.rocket.Rocket;
import cn.hikyson.rocket.callback.IErrorHandler;
import cn.hikyson.rocket.callback.ITasksFinishCallback;
import cn.hikyson.rocket.callback.ITimeoutHandler;
import cn.hikyson.rocket.task.ITailTask;
import cn.hikyson.rocket.task.LaunchTask;
import cn.hikyson.rocket.task.TailTask;
import cn.hikyson.rocket.task.TaskRecord;
import cn.hikyson.rocket.util.L;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Toast.makeText(SplashActivity.this, "running...", Toast.LENGTH_LONG).show();
        try {
            Rocket.get().tailTask(new ITailTask() {
                @Nullable
                @Override
                public String taskName() {
                    return TailTask.class.getSimpleName();
                }

                @NonNull
                @Override
                public Executor runOn() {
                    return cn.hikyson.rocket.util.Execs.io();
                }

                @Override
                public void beforeWait() {
                }

                @Override
                public void beforeRun() {
                }

                @Override
                public void onTaskDone(TaskRecord taskRecord) {
                }
            }).tasksFinishCallback(new ITasksFinishCallback() {
                @Override
                public void onTasksFinished() {
                    L.d("onTasksFinished.");
                }
            }).timeoutHandler(10000, new ITimeoutHandler() {
                @Override
                public void onTimeout(List<LaunchTask> timeoutTasks) {
                    L.d("onTimeout: " + String.valueOf(timeoutTasks));
                }
            }).errorHandler(new IErrorHandler() {
                @Override
                public void onError(LaunchTask task, Throwable e) {
                    L.d("onError: " + String.valueOf(task) + ", " + String.valueOf(e));
                }
            }).from(getApplication(), "rocket/task_list.xml").launch();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }
}
