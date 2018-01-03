package cn.hikyson.rocket.sample;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by kysonchao on 2017/12/28.
 */
public class Execs {
    public static Executor io = Executors.newCachedThreadPool();
}
