<p align="center">
  <img src="ART/web_hi_res_512.png" width="256" height="256" />
</p>

<h1 align="center">Rocket</h1>
<p align="center">
<a href="https://travis-ci.org/Kyson/Rocket" target="_blank"><img src="https://travis-ci.org/Kyson/Rocket.svg?branch=master"></img></a>
<a href="https://oss.sonatype.org/content/repositories/releases/cn/hikyson/rocket/rocket-core/" target="_blank"><img src="https://img.shields.io/maven-central/v/cn.hikyson.rocket/rocket-core.svg"></img></a>
<a href="https://jitpack.io/#Kyson/Rocket" target="_blank"><img src="https://jitpack.io/v/Kyson/Rocket.svg"></img></a>
<a href="LICENSE" target="_blank"><img src="http://img.shields.io/badge/license-Apache2.0-brightgreen.svg?style=flat"></img></a>
</p>
<br/>

> Android启动任务调度框架，用于缩短app启动时间

## 快速开始

### STEP0

引入依赖，使用gradle

```
dependencies {
  implementation 'cn.hikyson.rocket:rocket-core:VERSION_NAME'
}
```

### STEP1

定义某一个任务：

```
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
        Thread.sleep(6000);
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
        //执行的线程，默认执行在rocket自带的适合大部分app的线程池中
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
```

任务的每个接口表示含义见注释。

### STEP2

**所有API均由Rocket类提供**

注册任务有两种方式：

1. `public synchronized Rocket from(Application application, final List<LaunchTask> conditionTasks)`接口直接传入直接定义的任务列表
2. `public synchronized Rocket from(Application application, String assetFile)`，从assets文件中实例化任务列表，具体方式：

在应用assets目录下创建任务列表的xml格式文件，文件根节点为task-list，任务节点名为task，比如在assets下创建rocket文件夹，并在此创建task_list.xml文件，文件内容为：

```
<?xml version="1.0" encoding="utf-8"?>
<task-list>
    <task name="test1">cn.hikyson.rocket.sample.TestTask1</task>
    <task name="test2">cn.hikyson.rocket.sample.TestTask2</task>
    <task name="test3">cn.hikyson.rocket.sample.TestTask3</task>
    <task name="test4">cn.hikyson.rocket.sample.TestTask4</task>
    <task name="test5">cn.hikyson.rocket.sample.TestTask5</task>
</task-list>
```

> name属性可写可不写，仅仅做个声明，如果声明了，那么必须和taskName()保持一致。

### STEP3

在application中开始Rocket：

```
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
                          })
            .timeoutHandler(10000, new ITimeoutHandler() {
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
```

- tasksFinishCallback接口为所有的任务结束回调
- 如果调用了tailTask接口，Rocket会在任务列表结尾添加TailTask任务，如果你希望任务M在所有任务结束之后再执行，那么可以调用这个接口并且声明M任务依赖这个TailTask。
- timeoutHandler用于超时回调，Rocket会检测所有的任务执行状态，如果达到了超时时间且部分任务没有完成就会回调这个方法，并返回未完成的任务列表
- errorHandler用于错误回调，如果有些任务发生异常则回调此方法
- from接口声明Rocket从哪里读取任务列表，比如读取STEP2创建的assets目录下的任务列表文件
- 最后调用launch开始执行任务

### STEP4

如果首页需要等待某几个必要任务执行完毕才能开始使用，可以使用`public static void ensureTasks(String... taskNames)`接口，这个接口后的代码会一直等待直到任务完成才能执行。

更便捷的做法：在Activity上声明`RocketDependency`注解

```
//页面依赖test2的任务
@RocketDependency({"test2"})
public class SecondActivity extends AbsActivity {
}
```

## 详细说明

[Rocket-Android启动任务调度框架](https://github.com/Kyson/Rocket/blob/master/Rocket-Android%E5%90%AF%E5%8A%A8%E4%BB%BB%E5%8A%A1%E8%B0%83%E5%BA%A6%E6%A1%86%E6%9E%B6.md)