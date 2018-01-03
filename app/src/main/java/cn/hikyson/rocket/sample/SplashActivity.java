package cn.hikyson.rocket.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import cn.hikyson.rocket.Rocket;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);
    }

    public void test(View view) {
        Toast.makeText(SplashActivity.this, "running...", Toast.LENGTH_LONG).show();
        try {
            Rocket.get().from(SplashActivity.this, "rocket/task_list.xml").launch();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
