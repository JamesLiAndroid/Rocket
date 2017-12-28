package cn.hikyson.rocket.util;

import android.util.Log;

/**
 * Created by kysonchao on 2017/12/27.
 */
public class L {
    private static final String DEFAULT_TAG = "rocket";

    public interface LogProxy {
        void d(String msg);

        void e(String msg);
    }

    private static LogProxy sLogProxy;

    public static void setProxy(LogProxy logProxy) {
        sLogProxy = logProxy;
    }

    private static LogProxy getLogProxy() {
        if (sLogProxy == null) {
            return new LogProxy() {

                @Override
                public void d(String msg) {
                    Log.d(DEFAULT_TAG, msg);
                }

                @Override
                public void e(String msg) {
                    Log.e(DEFAULT_TAG, msg);
                }
            };
        }
        return sLogProxy;
    }


    public static void d(String msg) {
        getLogProxy().d(msg);
    }

    public static void e(String msg) {
        getLogProxy().e(msg);
    }
}
