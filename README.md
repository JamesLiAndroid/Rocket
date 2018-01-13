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
```

任务的每个接口表示含义见注释。

### STEP2

注册任务，在应用assets目录下创建任务列表的xml格式文件，文件根节点为task-list，任务节点名为task，比如在assets下创建rocket文件夹，并在此创建task_list.xml文件，文件内容为：

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
```

 - 如果调用了tailTask接口，Rocket会在任务列表结尾添加TailTask任务，如果你希望任务M在所有任务结束之后再执行，那么可以调用这个接口并且声明M任务依赖这个TailTask。
- timeoutHandler用于超时回调，Rocket会检测所有的任务执行状态，如果达到了超时时间且部分任务没有完成就会回调这个方法，并返回未完成的任务列表
- errorHandler用于错误回调，如果有些任务发生异常则回调此方法
- from接口声明Rocket从哪里读取任务列表，比如读取STEP2创建的assets目录下的任务列表文件
- 最后调用launch开始执行任务

## 详细说明

[Rocket:如何最大程度且持续地压榨Android-App启动耗时](https://github.com/Kyson/Rocket/blob/master/Rocket-%E5%A6%82%E4%BD%95%E6%9C%80%E5%A4%A7%E7%A8%8B%E5%BA%A6%E4%B8%94%E6%8C%81%E7%BB%AD%E5%9C%B0%E5%8E%8B%E6%A6%A8Android-App%E5%90%AF%E5%8A%A8%E8%80%97%E6%97%B6.md)