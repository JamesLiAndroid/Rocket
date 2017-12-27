package cn.hikyson.rocket.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import cn.hikyson.rocket.ConditionTask;
import cn.hikyson.rocket.TaskScheduer;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);
    }

    public void test(View view) {
        CountDownLatch downLatch = new CountDownLatch(0);
        try {
            downLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Toast.makeText(SplashActivity.this, "running...", Toast.LENGTH_LONG).show();
        final Executor executor = Executors.newCachedThreadPool();
        ConditionTask conditionTask0 = new ConditionTask() {
            @Override
            public String taskName() {
                return "conditionTask0";
            }

            @Override
            public void run() throws InterruptedException {
                Thread.sleep(3000);
            }

            @Override
            public List<String> dependsOn() {
                return new ArrayList<>();
            }

            @Override
            public Executor runOn() {
                return executor;
            }
        };
        ConditionTask conditionTask1 = new ConditionTask() {
            @Override
            public String taskName() {
                return "conditionTask1";
            }

            @Override
            public void run() throws InterruptedException {
                Thread.sleep(1000);
            }

            @Override
            public List<String> dependsOn() {
                return Arrays.asList("conditionTask0");
            }

            @Override
            public Executor runOn() {
                return executor;
            }
        };
        ConditionTask conditionTask2 = new ConditionTask() {
            @Override
            public String taskName() {
                return "conditionTask2";
            }

            @Override
            public void run() throws InterruptedException {
                Thread.sleep(1000);
            }

            @Override
            public List<String> dependsOn() {
                return Arrays.asList("conditionTask0");
            }

            @Override
            public Executor runOn() {
                return executor;
            }
        };
        ConditionTask conditionTask3 = new ConditionTask() {
            @Override
            public String taskName() {
                return "conditionTask3";
            }

            @Override
            public void run() throws InterruptedException {
                Thread.sleep(5000);
            }

            @Override
            public List<String> dependsOn() {
                return Arrays.asList("conditionTask2");
            }

            @Override
            public Executor runOn() {
                return executor;
            }
        };
        ConditionTask conditionTask4 = new ConditionTask() {
            @Override
            public String taskName() {
                return "conditionTask4";
            }

            @Override
            public void run() throws InterruptedException {
                Thread.sleep(1000);
            }

            @Override
            public List<String> dependsOn() {
                return Arrays.asList("conditionTask2", "conditionTask3");
            }

            @Override
            public Executor runOn() {
                return executor;
            }
        };
//        ConditionTask conditionTask5 = new ConditionTask() {
//            @Override
//            public String taskName() {
//                return "conditionTask5";
//            }
//
//            @Override
//            public void run() throws InterruptedException {
//                Thread.sleep(1000);
//            }
//
//            @Override
//            public List<String> dependsOn() {
//                return Arrays.asList("conditionTask1");
//            }
//
//            @Override
//            public Executor runOn() {
//                return executor;
//            }
//        };
//        ConditionTask conditionTask6 = new ConditionTask() {
//            @Override
//            public String taskName() {
//                return "conditionTask6";
//            }
//
//            @Override
//            public void run() throws InterruptedException {
//                Thread.sleep(1000);
//            }
//
//            @Override
//            public List<String> dependsOn() {
//                return Arrays.asList("conditionTask1", "conditionTask2", "conditionTask5");
//            }
//
//            @Override
//            public Executor runOn() {
//                return executor;
//            }
//        };
//        ConditionTask conditionTask7 = new ConditionTask() {
//            @Override
//            public String taskName() {
//                return "conditionTask7";
//            }
//
//            @Override
//            public void run() throws InterruptedException {
//                Thread.sleep(1000);
//            }
//
//            @Override
//            public List<String> dependsOn() {
//                return Arrays.asList("conditionTask1", "conditionTask2");
//            }
//
//            @Override
//            public Executor runOn() {
//                return executor;
//            }
//        };
//        ConditionTask conditionTask8 = new ConditionTask() {
//            @Override
//            public String taskName() {
//                return "conditionTask8";
//            }
//
//            @Override
//            public void run() throws InterruptedException {
//                Thread.sleep(1000);
//            }
//
//            @Override
//            public List<String> dependsOn() {
//                return Arrays.asList("conditionTask7");
//            }
//
//            @Override
//            public Executor runOn() {
//                return executor;
//            }
//        };
//        ConditionTask conditionTask9 = new ConditionTask() {
//            @Override
//            public String taskName() {
//                return "conditionTask9";
//            }
//
//            @Override
//            public void run() throws InterruptedException {
//                Thread.sleep(1000);
//            }
//
//            @Override
//            public List<String> dependsOn() {
//                return Arrays.asList("conditionTask7", "conditionTask8");
//            }
//
//            @Override
//            public Executor runOn() {
//                return executor;
//            }
//        };
//        TaskScheduer taskScheduer = new TaskScheduer(Arrays.asList(conditionTask0, conditionTask1, conditionTask2, conditionTask3, conditionTask4, conditionTask5, conditionTask6, conditionTask7, conditionTask8, conditionTask9));
        TaskScheduer taskScheduer = new TaskScheduer(Arrays.asList(conditionTask4, conditionTask0, conditionTask2, conditionTask1, conditionTask3));
        taskScheduer.schedule();
//        .subscribe(new DisposableObserver() {
//            @Override
//            public void onNext(Object o) {
//                Log.d("kyson", "onNext");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.d("kyson", "onError" + e.getLocalizedMessage());
//            }
//
//            @Override
//            public void onComplete() {
//                Log.d("kyson", "onComplete");
//            }
//        });
    }

    private static class TestThread extends Thread {

        public TestThread() {
        }

        @Override
        public void run() {

        }
    }
}
