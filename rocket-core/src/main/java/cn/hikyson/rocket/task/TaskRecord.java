package cn.hikyson.rocket.task;

/**
 * Created by kysonchao on 2018/1/4.
 */
public class TaskRecord {
    public long waitCostTime;
    public long runCostTime;
    public long runThreadCostTime;

    TaskRecord(long waitTime, long runTime, long runThreadTime, long doneTime, long doneThreadTime) {
        waitCostTime = runTime - waitTime;
        runCostTime = doneTime - runTime;
        runThreadCostTime = doneThreadTime - runThreadTime;
    }

    @Override
    public String toString() {
        return "TaskTimeRecord{" +
                "waitDuration=" + waitCostTime +
                "ms, runDuration=" + runCostTime +
                "ms, runThreadDuration=" + runThreadCostTime +
                "ms}";
    }
}
