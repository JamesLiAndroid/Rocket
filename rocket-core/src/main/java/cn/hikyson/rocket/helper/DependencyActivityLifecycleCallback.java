package cn.hikyson.rocket.helper;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;

import cn.hikyson.rocket.util.L;

/**
 * 具有依赖任务的页面回调
 * Created by kysonchao on 2018/1/5.
 */
public class DependencyActivityLifecycleCallback implements Application.ActivityLifecycleCallbacks {

    private OnCreateAndDependencyParsedCallback mOnCreateAndDependencyParsedCallback;

    public DependencyActivityLifecycleCallback(@NonNull OnCreateAndDependencyParsedCallback onCreateAndDependencyParsedCallback) {
        mOnCreateAndDependencyParsedCallback = onCreateAndDependencyParsedCallback;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        long createdTime = System.currentTimeMillis();
        String[] dependencies = parseClassDependencies(activity);
        if (dependencies != null && dependencies.length > 0) {
            mOnCreateAndDependencyParsedCallback.onCreateAndDependencyParsed(dependencies);
        }
        L.d(activity.getClass().getSimpleName() + " onActivityCreated cost " + (System.currentTimeMillis() - createdTime) + " ms");
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    private static String[] parseClassDependencies(Object o) {
        RocketDependency rocketDependency = o.getClass().getAnnotation(RocketDependency.class);
        if (rocketDependency != null) {
            return rocketDependency.value();
        }
        return null;
    }
}