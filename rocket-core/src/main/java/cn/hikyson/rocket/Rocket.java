package cn.hikyson.rocket;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import cn.hikyson.rocket.parser.TaskParser;
import cn.hikyson.rocket.task.ConditionTask;
import cn.hikyson.rocket.task.ITailTask;
import cn.hikyson.rocket.task.TailTask;
import cn.hikyson.rocket.task.TaskScheduer;
import cn.hikyson.rocket.util.L;

/**
 * 入口类
 * Created by kysonchao on 2017/12/26.
 */
public class Rocket {
    private List<ConditionTask> mConditionTasks;
    private Map<String, ConditionTask> mTaskNameMap;
    private ITailTask mITailTask;

    private Rocket() {
    }

    private static class InstanceHolder {
        private static Rocket sInstance = new Rocket();
    }

    public static Rocket get() {
        return InstanceHolder.sInstance;
    }

    public synchronized Rocket from(Context context, String assetFile) throws Throwable {
        return from(TaskParser.parse(context, assetFile));
    }

    public synchronized Rocket from(final List<ConditionTask> conditionTasks) {
        mConditionTasks = new ArrayList<>(conditionTasks);
        if (mITailTask != null) {
            TailTask tailTask = new TailTask() {
                @NonNull
                @Override
                public List<String> dependsOn() {
                    //如果用户任务依赖尾部任务，那么尾部任务就不依赖该用户
                    return parseTaskNames(conditionTasks, new TaskFilter() {
                        @Override
                        public boolean filter(ConditionTask task) {
                            return !task.dependsOn().contains(TailTask.class.getSimpleName());
                        }
                    });
                }

                @NonNull
                @Override
                public String taskName() {
                    if (TextUtils.isEmpty(mITailTask.taskName())) {
                        return super.taskName();
                    }
                    //noinspection ConstantConditions
                    return mITailTask.taskName();
                }

                @NonNull
                @Override
                public Executor runOn() {
                    return mITailTask.runOn();
                }
            };
            mConditionTasks.add(tailTask);
        }
        L.d("原始任务列表: " + String.valueOf(mConditionTasks));
        mTaskNameMap = Collections.synchronizedMap(new HashMap<String,ConditionTask>());
        for (int i = 0; i < mConditionTasks.size(); i++) {
            ConditionTask task = mConditionTasks.get(i);
            mTaskNameMap.put(task.taskName(), task);
        }
        return this;
    }

    public synchronized void launch() {
        if (mConditionTasks == null) {
            throw new IllegalStateException("init task list first");
        }
        new TaskScheduer(mConditionTasks).schedule();
    }

    /**
     * 确保任务完成，block直到任务完成
     *
     * @param taskName
     */
    public static void ensureTask(String taskName) {
        Map<String, ConditionTask> taskNameMap = Rocket.get().mTaskNameMap;
        if (taskNameMap == null) {
            return;
        }
        ConditionTask task = taskNameMap.get(taskName);
        if (task == null) {
            throw new IllegalStateException("can not find task " + taskName);
        }
        try {
            task.waitDone();
        } catch (InterruptedException e) {
            L.e(String.valueOf(e));
        }
    }

    /**
     * 确保任务完成，block直到任务完成
     *
     * @param taskNames
     */
    public static void ensureTasks(String... taskNames) {
        for (String taskName : taskNames) {
            ensureTask(taskName);
        }
    }

    /**
     * 配置尾部task
     *
     * @param iTailTask
     * @return
     */
    public Rocket tailTask(ITailTask iTailTask) {
        this.mITailTask = iTailTask;
        return this;
    }

    private interface TaskFilter {
        boolean filter(ConditionTask task);
    }

    private synchronized List<String> parseTaskNames(List<ConditionTask> conditionTasks, TaskFilter taskFilter) {
        List<String> taskNames = new ArrayList<>();
        for (ConditionTask task : conditionTasks) {
            if (taskFilter == null || taskFilter.filter(task)) {
                taskNames.add(task.taskName());
            }
        }
        return taskNames;
    }
}
