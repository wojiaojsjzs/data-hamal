package com.striveonger.study.task.common.scope.context;

import com.striveonger.study.task.common.constant.StepStatus;

/**
 * @author Mr.Lee
 * @description: Step运行时环境
 * @date 2023-05-05 22:33
 */
public class StepContext {

    private final int index;

    private String stepID;

    private String displayName;

    private TaskContext taskContext;

    public StepContext(int index) {
        this.index = index;
    }

    public String getTaskID() {
        return taskContext.getTaskID();
    }

    public int getIndex() {
        return index;
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

    public TaskContext getTaskContext() {
        return taskContext;
    }

    public void setTaskContext(TaskContext taskContext) {
        this.taskContext = taskContext;
    }

    public void updateRuntimeStatus(StepStatus status) {
        taskContext.updateRuntimeStatus(index, status);
    }
}
