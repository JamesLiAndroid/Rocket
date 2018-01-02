package cn.hikyson.rocket.util;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by kysonchao on 2018/1/2.
 */
public class Execs {
    public static Executor MAIN_EXECUTOR = new MainThreadExecutor();
    public static Executor SINGLE = Executors.newSingleThreadExecutor();
}
