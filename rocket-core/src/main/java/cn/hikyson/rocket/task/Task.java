package cn.hikyson.rocket.task;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * Created by kysonchao on 2017/12/26.
 */
public interface Task {
    /**
     * 任务名称，唯一标示
     *
     * @return
     */
    String taskName();

    /**
     * 执行task
     */
    void run() throws Throwable;

    /**
     * task依赖的其他task
     *
     * @return
     */
    List<String> dependsOn();

    /**
     * task执行的任务调度器
     *
     * @return
     */
    Executor runOn();
}
