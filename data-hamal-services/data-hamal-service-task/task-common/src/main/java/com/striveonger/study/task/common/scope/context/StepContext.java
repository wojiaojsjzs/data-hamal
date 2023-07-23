package com.striveonger.study.task.common.scope.context;

import com.striveonger.study.task.common.constant.StepStatus;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

    /**
     * 获取前驱组件的ID (前驱组件可能有多个)
     */
    public Collection<String> prev() {
        return taskContext.getPrev().get(stepID);
    }

    /**
     * 获取后继组件的ID (后继组件可能有多个)
     */
    public Collection<String> next() {
        return taskContext.getNext().get(stepID);
    }
}
