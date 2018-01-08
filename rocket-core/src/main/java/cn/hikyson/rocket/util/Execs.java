package cn.hikyson.rocket.util;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by kysonchao on 2018/1/2.
 */
public class Execs {
    private static Executor sMain;
    private static Executor sIo;
    private static Executor sSingle = Executors.newSingleThreadExecutor();

    public static Executor io() {
        if (sIo == null) {
            sIo = Executors.newCachedThreadPool();
        }
        return sIo;
    }

    public static Executor main() {
        if (sMain == null) {
            sMain = new MainThreadExecutor();
        }
        return sMain;
    }
}
