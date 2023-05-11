package com.striveonger.study.task.core.executor.extra;

import com.striveonger.study.task.core.executor.Executor;

/**
 * @author Mr.Lee
 * @description: Executor 扩展的信息(用于构建task)
 * @date 2023-05-12 00:29
 */
public class ExecutorExtraInfo {
    // 扩展内容 start...
    private long stepID;
    private String displayName;
    // 扩展内容 end...

    private Executor executor;

    public long getStepID() {
        return stepID;
    }

    public void setStepID(long stepID) {
        this.stepID = stepID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }
}
