package cn.hikyson.rocket.task;

/**
 * Created by kysonchao on 2018/1/4.
 */
public class TaskRecord {
    private long waitDuration;
    private long runDuration;
    private long runThreadDuration;

    TaskRecord(long waitTime, long runTime, long runThreadTime, long doneTime, long doneThreadTime) {
        waitDuration = runTime - waitTime;
        runDuration = doneTime - runTime;
        runThreadDuration = doneThreadTime - runThreadTime;
    }

    @Override
    public String toString() {
        return "TaskTimeRecord{" +
                "waitDuration=" + waitDuration +
                "ms, runDuration=" + runDuration +
                "ms, runThreadDuration=" + runThreadDuration +
                "ms}";
    }
}
