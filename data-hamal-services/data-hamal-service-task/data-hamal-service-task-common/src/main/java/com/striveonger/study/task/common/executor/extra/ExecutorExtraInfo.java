package com.striveonger.study.task.common.executor.extra;

import com.striveonger.study.task.common.executor.Executable;

/**
 * @author Mr.Lee
 * @description: Executor 扩展的信息(用于构建task)
 * @date 2023-05-12 00:29
 */
public class ExecutorExtraInfo {
    // 扩展内容 start...
    private int index;
    private String stepID;
    private String displayName;
    // 扩展内容 end...

    private Executable executor;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

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

    public Executable getExecutor() {
        return executor;
    }

    public void setExecutor(Executable executor) {
        this.executor = executor;
    }
}
