package cn.hikyson.rocket.sample;

import android.os.Process;
import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

import cn.hikyson.rocket.task.LaunchTask;
import cn.hikyson.rocket.task.TaskRecord;

/**
 * Created by kysonchao on 2017/12/28.
 */
public class TestTask1 extends LaunchTask {
    @NonNull
    @Override
    public String taskName() {
        //任务名称，默认使用类名
        return "test1";
    }


    @Override
    public void run() throws Throwable {
        //执行的任务
        Thread.sleep(2000);
    }

    @NonNull
    @Override
    public List<String> dependsOn() {
        //依赖的其他任务
        return Collections.emptyList();
    }

    @NonNull
    @Override
    public Executor runOn() {
        //执行的线程
        return Execs.io;
    }

    @Override
    public int priority() {
        //线程优先级，默认Process.THREAD_PRIORITY_BACKGROUND + Process.THREAD_PRIORITY_MORE_FAVORABLE
        return super.priority() + Process.THREAD_PRIORITY_MORE_FAVORABLE;
    }

    @Override
    public void beforeWait() {
        //任务分为三个阶段：wait -> run -> done
        //任务处于等待状态的回调
        super.beforeWait();
    }

    @Override
    public void beforeRun() {
        //任务分为三个阶段：wait -> run -> done
        //任务处于运行状态的回调
        super.beforeRun();
    }

    @Override
    public void onTaskDone(TaskRecord taskRecord) {
        //任务分为三个阶段：wait -> run -> done
        //任务处于运行完成状态的回调，TaskRecord记录了任务的执行时间等信息
        super.onTaskDone(taskRecord);
    }
}
