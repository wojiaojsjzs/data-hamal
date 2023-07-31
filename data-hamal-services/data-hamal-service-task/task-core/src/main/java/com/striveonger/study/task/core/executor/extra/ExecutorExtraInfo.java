package com.striveonger.study.task.core.executor.extra;

import com.striveonger.study.task.core.executor.Executor;

/**
 * @author Mr.Lee
 * @description: Executor 扩展的信息(用于构建task)
 * @date 2023-05-12 00:29
 */
public class ExecutorExtraInfo {
    // 扩展内容 start...
    private String stepID;
    private String displayName;
    private String type;
    // 扩展内容 end...

    private Executor executor;


    public String getStepID() {
        return stepID;
    }

    public void setStepID(String stepID) {
        this.stepID = stepID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }
}
