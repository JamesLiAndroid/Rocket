package cn.hikyson.rocket;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import cn.hikyson.rocket.task.ConditionTask;

/**
 * Created by kysonchao on 2018/1/2.
 */
public class TaskLock {

    private Map<String, CountDownLatch> mTaskLocks;

    public TaskLock(List<ConditionTask> tasks) {
        mTaskLocks = Collections.synchronizedMap(new HashMap<String, CountDownLatch>());
        for (ConditionTask task : tasks) {
            mTaskLocks.put(task.taskName(), new CountDownLatch(1));
        }
    }

    public void onTaskDone(String taskName) {
        CountDownLatch countDownLatch = mTaskLocks.remove(taskName);
        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
    }

    public void waitTaskDone(String taskName) throws InterruptedException {
        CountDownLatch countDownLatch = mTaskLocks.get(taskName);
        if (countDownLatch != null) {
            countDownLatch.await();
        }
    }
}
