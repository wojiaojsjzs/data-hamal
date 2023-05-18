package com.striveonger.study.task.core.scope.context;

/**
 * @author Mr.Lee
 * @description: Step运行时环境
 * @date 2023-05-05 22:33
 */
public class StepContext {

    private String stepID;

    private String displayName;

    private TaskContext taskContext;

    public long getTaskID() {
        return taskContext.getTaskID();
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
}