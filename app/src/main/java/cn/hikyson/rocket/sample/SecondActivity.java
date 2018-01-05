package cn.hikyson.rocket.sample;

import android.os.Bundle;

import cn.hikyson.rocket.helper.RocketDependency;

@RocketDependency({"test2"})
public class SecondActivity extends AbsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }
}
