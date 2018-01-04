package cn.hikyson.rocket.task;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import cn.hikyson.rocket.BuildConfig;

/**
 * Created by kysonchao on 2017/12/28.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class TaskScheduerTest {
    @Test
    public void schedule1() throws Exception {
        final Executor executor = Executors.newCachedThreadPool();
        final CountDownLatch downLatch = new CountDownLatch(4);
        final long[] taskStartTime = {0, 0};
        final long[] taskEndTime = {0, 0};
        LaunchTask task0 = TaskFactory.create("task0", 1000, new ArrayList<String>(), executor, new TaskCallback() {
            @Override
            public void taskStart() {
                taskStartTime[0] = System.nanoTime();
                downLatch.countDown();
            }

            @Override
            public void taskEnd() {
                taskEndTime[0] = System.nanoTime();
                downLatch.countDown();
            }
        });
        LaunchTask task1 = TaskFactory.create("task1", 1000, Collections.singletonList("task0"), executor, new TaskCallback() {
            @Override
            public void taskStart() {
                taskStartTime[1] = System.nanoTime();
                downLatch.countDown();
            }

            @Override
            public void taskEnd() {
                taskEndTime[1] = System.nanoTime();
                downLatch.countDown();
            }
        });
        new TaskScheduer(Arrays.<LaunchTask>asList(task0, task1)).schedule();
        downLatch.await();
        //1 依赖 0
        Assert.assertTrue(taskStartTime[1] > taskEndTime[0]);
    }


    @Test
    public void schedule2() throws Exception {
        final Executor executor = Executors.newCachedThreadPool();
        final CountDownLatch downLatch = new CountDownLatch(6);
        final long[] taskStartTime = {0, 0, 0};
        final long[] taskEndTime = {0, 0, 0};
        LaunchTask task0 = TaskFactory.create("task0", 1000, new ArrayList<String>(), executor, new TaskCallback() {
            @Override
            public void taskStart() {
                taskStartTime[0] = System.nanoTime();
                downLatch.countDown();
            }

            @Override
            public void taskEnd() {
                taskEndTime[0] = System.nanoTime();
                downLatch.countDown();
            }
        });
        LaunchTask task1 = TaskFactory.create("task1", 1000, Collections.singletonList("task0"), executor, new TaskCallback() {
            @Override
            public void taskStart() {
                taskStartTime[1] = System.nanoTime();
                downLatch.countDown();
            }

            @Override
            public void taskEnd() {
                taskEndTime[1] = System.nanoTime();
                downLatch.countDown();
            }
        });
        LaunchTask task2 = TaskFactory.create("task2", 1000, Collections.singletonList("task0"), executor, new TaskCallback() {
            @Override
            public void taskStart() {
                taskStartTime[2] = System.nanoTime();
                downLatch.countDown();
            }

            @Override
            public void taskEnd() {
                taskEndTime[2] = System.nanoTime();
                downLatch.countDown();
            }
        });
        new TaskScheduer(Arrays.asList(task0, task1, task2)).schedule();
        downLatch.await();
        //1 依赖 0 ,2 依赖 0
        Assert.assertTrue(taskEndTime[0] < taskStartTime[1]);
        Assert.assertTrue(taskEndTime[0] < taskStartTime[2]);
    }

    @Test
    public void schedule3() throws Exception {
        final Executor executor = Executors.newCachedThreadPool();
        final CountDownLatch downLatch = new CountDownLatch(6);
        final long[] taskStartTime = {0, 0, 0};
        final long[] taskEndTime = {0, 0, 0};
        LaunchTask task0 = TaskFactory.create("task0", 1000, new ArrayList<String>(), executor, new TaskCallback() {
            @Override
            public void taskStart() {
                taskStartTime[0] = System.nanoTime();
                downLatch.countDown();
            }

            @Override
            public void taskEnd() {
                taskEndTime[0] = System.nanoTime();
                downLatch.countDown();
            }
        });
        LaunchTask task1 = TaskFactory.create("task1", 1000, Collections.singletonList("task0"), executor, new TaskCallback() {
            @Override
            public void taskStart() {
                taskStartTime[1] = System.nanoTime();
                downLatch.countDown();
            }

            @Override
            public void taskEnd() {
                taskEndTime[1] = System.nanoTime();
                downLatch.countDown();
            }
        });
        LaunchTask task2 = TaskFactory.create("task2", 1000, Arrays.asList("task0", "task1"), executor, new TaskCallback() {
            @Override
            public void taskStart() {
                taskStartTime[2] = System.nanoTime();
                downLatch.countDown();
            }

            @Override
            public void taskEnd() {
                taskEndTime[2] = System.nanoTime();
                downLatch.countDown();
            }
        });
        new TaskScheduer(Arrays.asList(task2, task1, task0)).schedule();
        downLatch.await();
        //1 依赖 0 ,2 依赖 1 ,2 依赖0
        Assert.assertTrue(taskEndTime[0] < taskStartTime[1]);
        Assert.assertTrue(taskEndTime[1] < taskStartTime[2]);
    }


    @Test
    public void schedule4() throws Exception {
        final Executor executor = Executors.newCachedThreadPool();
        LaunchTask task0 = TaskFactory.create("task0", 1000, Collections.singletonList("task2"), executor, new TaskCallback() {
            @Override
            public void taskStart() {
            }

            @Override
            public void taskEnd() {
            }
        });
        LaunchTask task1 = TaskFactory.create("task1", 1000, Collections.singletonList("task0"), executor, new TaskCallback() {
            @Override
            public void taskStart() {
            }

            @Override
            public void taskEnd() {
            }
        });
        LaunchTask task2 = TaskFactory.create("task2", 1000, Collections.singletonList("task1"), executor, new TaskCallback() {
            @Override
            public void taskStart() {
            }

            @Override
            public void taskEnd() {
            }
        });
        try {
            new TaskScheduer(Arrays.asList(task2, task1, task0)).schedule();
        } catch (Throwable throwable) {
            Assert.assertTrue(throwable instanceof IllegalStateException);
            Assert.assertTrue(throwable.getLocalizedMessage().equals("exists a cycle in the graph"));
            return;
        }
        Assert.assertTrue(false);
    }


}