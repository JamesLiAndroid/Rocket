package cn.hikyson.rocket.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import cn.hikyson.rocket.Rocket;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Toast.makeText(SplashActivity.this, "running...", Toast.LENGTH_LONG).show();
        try {
            Rocket.get().from(getApplication(), "rocket/task_list.xml").launch();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }
}
