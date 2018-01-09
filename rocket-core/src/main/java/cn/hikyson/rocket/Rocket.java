package cn.hikyson.rocket;

import android.app.Application;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import cn.hikyson.rocket.exception.IErrorHandler;
import cn.hikyson.rocket.exception.ITimeoutHandler;
import cn.hikyson.rocket.helper.DependencyActivityLifecycleCallback;
import cn.hikyson.rocket.helper.OnCreateAndDependencyParsedCallback;
import cn.hikyson.rocket.parser.TaskParser;
import cn.hikyson.rocket.task.ITailTask;
import cn.hikyson.rocket.task.LaunchTask;
import cn.hikyson.rocket.task.TailTask;
import cn.hikyson.rocket.task.TaskRecord;
import cn.hikyson.rocket.task.TaskScheduer;
import cn.hikyson.rocket.util.L;

/**
 * 入口类
 * Created by kysonchao on 2017/12/26.
 */
public class Rocket {
    private List<LaunchTask> mConditionTasks;
    private Map<String, LaunchTask> mTaskNameMap;
    private ITailTask mITailTask;
    private IErrorHandler mIErrorHandler;
    private ITimeoutHandler mITimeoutHandler;
    private long mTimeoutMillis;

    private Rocket() {
    }

    private static class InstanceHolder {
        private static Rocket sInstance = new Rocket();
    }

    public static Rocket get() {
        return InstanceHolder.sInstance;
    }

    public synchronized Rocket from(Application application, String assetFile) throws Throwable {
        return from(application, TaskParser.parse(application, assetFile));
    }

    public synchronized Rocket from(Application application, final List<LaunchTask> conditionTasks) {
        mConditionTasks = new ArrayList<>(conditionTasks);
        if (mITailTask != null) {//需要尾部task就添加上去
            mConditionTasks.add(createTailTask(mConditionTasks, mITailTask));
        }
        L.d("Origin task list: " + String.valueOf(mConditionTasks));
        //缓存任务名称和任务的mapping关系
        cacheTaskNameMap();
        application.registerActivityLifecycleCallbacks(new DependencyActivityLifecycleCallback(new OnCreateAndDependencyParsedCallback() {
            @Override
            public void onCreateAndDependencyParsed(@NonNull String[] dependencies) {
                Rocket.ensureTasks(dependencies);
            }
        }));
        return this;
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

    /**
     * 配置rocket中任务发生的异常
     *
     * @param iErrorHandler
     * @return
     */
    public Rocket errorHandler(IErrorHandler iErrorHandler) {
        this.mIErrorHandler = iErrorHandler;
        return this;
    }

    /**
     * 配置rocket的超时异常
     *
     * @param iTimeoutHandler
     * @return
     */
    public Rocket timeoutHandler(long timeoutMillis, ITimeoutHandler iTimeoutHandler) {
        this.mTimeoutMillis = timeoutMillis;
        this.mITimeoutHandler = iTimeoutHandler;
        return this;
    }

    /**
     * 开始执行任务
     */
    public synchronized void launch() {
        if (mConditionTasks == null) {
            throw new IllegalStateException("init task list first");
        }
        new TaskScheduer(mConditionTasks).schedule(this.mIErrorHandler, this.mTimeoutMillis, this.mITimeoutHandler);
    }

    /**
     * 确保任务完成，block直到任务完成
     *
     * @param taskName
     */
    public static void ensureTask(String taskName) {
        Map<String, LaunchTask> taskNameMap = Rocket.get().mTaskNameMap;
        if (taskNameMap == null) {
            throw new IllegalStateException("Init task list first");
        }
        LaunchTask task = taskNameMap.get(taskName);
        if (task == null) {
            throw new IllegalStateException("Can not find task " + taskName);
        }
        try {
            task.dependencyWait();
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

    private void cacheTaskNameMap() {
        mTaskNameMap = Collections.synchronizedMap(new HashMap<String, LaunchTask>());
        for (int i = 0; i < mConditionTasks.size(); i++) {
            LaunchTask task = mConditionTasks.get(i);
            mTaskNameMap.put(task.taskName(), task);
        }
    }

    private interface TaskFilter {
        boolean filter(LaunchTask task);
    }

    private static TailTask createTailTask(final List<LaunchTask> cts, final ITailTask iTailTask) {
        final List<String> taskNamesExcludeAfterTail = parseTaskNames(cts, new TaskFilter() {
            @Override
            public boolean filter(LaunchTask task) {
                return !task.dependsOn().contains(TailTask.class.getSimpleName());
            }
        });
        return new TailTask() {

            @NonNull
            @Override
            public List<String> dependsOn() {
                //如果用户任务依赖尾部任务，那么尾部任务就不依赖该用户
                return taskNamesExcludeAfterTail;
            }

            @NonNull
            @Override
            public String taskName() {
                if (TextUtils.isEmpty(iTailTask.taskName())) {
                    return super.taskName();
                }
                //noinspection ConstantConditions
                return iTailTask.taskName();
            }

            @NonNull
            @Override
            public Executor runOn() {
                return iTailTask.runOn();
            }

            @Override
            public void beforeWait() {
                iTailTask.beforeWait();
            }

            @Override
            public void beforeRun() {
                iTailTask.beforeRun();
            }

            @Override
            public void onTaskDone(TaskRecord taskRecord) {
                iTailTask.onTaskDone(taskRecord);
            }
        };
    }

    private static List<String> parseTaskNames(List<LaunchTask> conditionTasks, TaskFilter taskFilter) {
        List<String> taskNames = new ArrayList<>();
        for (LaunchTask task : conditionTasks) {
            if (taskFilter == null || taskFilter.filter(task)) {
                taskNames.add(task.taskName());
            }
        }
        return taskNames;
    }
}
