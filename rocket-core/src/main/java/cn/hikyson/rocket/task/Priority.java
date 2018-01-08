package cn.hikyson.rocket.task;

import android.os.Process;
import android.support.annotation.IntRange;

/**
 * Created by kysonchao on 2018/1/3.
 */
public interface Priority {
    @IntRange(from = Process.THREAD_PRIORITY_FOREGROUND, to = Process.THREAD_PRIORITY_LOWEST)
    int priority();
}
