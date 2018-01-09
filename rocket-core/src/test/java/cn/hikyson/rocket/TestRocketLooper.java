package cn.hikyson.rocket;

import android.os.Looper;

import org.robolectric.annotation.Implements;
import org.robolectric.shadows.ShadowLooper;

/**
 * Created by kysonchao on 2018/1/9.
 */
@Implements(value = Looper.class)
public class TestRocketLooper extends ShadowLooper {
}
