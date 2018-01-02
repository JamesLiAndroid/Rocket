package cn.hikyson.rocket.task;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;

/**
 * Created by kysonchao on 2018/1/2.
 */
@Keep
public abstract class TailTask extends ConditionTask {

    @NonNull
    @Override
    public String taskName() {
        return TailTask.class.getSimpleName();
    }

    @Override
    public void run() throws Throwable {
        //do nothing
    }
}
