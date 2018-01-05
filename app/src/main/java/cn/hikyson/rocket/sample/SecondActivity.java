package cn.hikyson.rocket.sample;

import android.app.Activity;
import android.os.Bundle;

import cn.hikyson.rocket.helper.RocketDependency;

@RocketDependency({"test1", "test2"})
public class SecondActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }
}
