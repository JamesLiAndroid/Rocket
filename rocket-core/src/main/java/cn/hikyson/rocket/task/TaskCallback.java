package cn.hikyson.rocket.task;

/**
 * Created by kysonchao on 2018/1/2.
 */
public interface TaskCallback {
    //任务开始执行，处于等待条件允许状态
    void beforeWait();

    //任务条件允许，开始执行
    void beforeRun();

    //任务执行完毕
    void onTaskDone(TaskRecord taskRecord);
}
