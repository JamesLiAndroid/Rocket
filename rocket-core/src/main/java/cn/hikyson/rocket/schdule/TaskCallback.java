package cn.hikyson.rocket.schdule;

import cn.hikyson.rocket.task.ConditionTask;

/**
 * Created by kysonchao on 2018/1/2.
 */
public interface TaskCallback {
    //任务开始执行，处于等待条件允许状态
    void taskWait(ConditionTask task);

    //任务条件允许，开始执行
    void taskRun(ConditionTask task);

    //任务执行完毕
    void taskDone(ConditionTask task);
}
