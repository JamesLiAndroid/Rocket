package cn.hikyson.rocket.schdule;


import android.support.v4.util.ArrayMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by kysonchao on 2018/1/2.
 */
public class TaskCallbacks {
    private Map<String, List<TaskCallback>> mTaskCallbackMap;

    public void addCallback(String taskName, TaskCallback taskCallback) {
        if (mTaskCallbackMap == null) {
            mTaskCallbackMap = new ArrayMap<>();
        }
        List<TaskCallback> taskCallbacks = mTaskCallbackMap.get(taskName);
        if (taskCallbacks == null) {
            taskCallbacks = new ArrayList<>();
            mTaskCallbackMap.put(taskName, taskCallbacks);
        }
        taskCallbacks.add(taskCallback);
    }

    public List<TaskCallback> removeCallback(String taskName) {
        if (mTaskCallbackMap == null) {
            return null;
        }
        return mTaskCallbackMap.remove(taskName);
    }

}
