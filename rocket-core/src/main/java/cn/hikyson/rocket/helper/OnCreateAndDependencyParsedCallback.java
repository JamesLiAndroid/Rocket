package cn.hikyson.rocket.helper;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * Created by kysonchao on 2018/1/5.
 */
public interface OnCreateAndDependencyParsedCallback {
    void onCreateAndDependencyParsed(Activity activity, Bundle savedInstanceState, @NonNull String[] dependencies);
}
