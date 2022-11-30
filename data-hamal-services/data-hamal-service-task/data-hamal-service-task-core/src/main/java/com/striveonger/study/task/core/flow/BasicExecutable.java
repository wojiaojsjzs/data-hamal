package com.striveonger.study.task.core.flow;

import com.striveonger.study.task.core.WorkArea;
import com.striveonger.study.task.core.Executable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Mr.Lee
 * @description: 基础的执行器
 * @date 2022-11-28 21:23
 */
public abstract class BasicExecutable implements Executable {
    private final Logger log = LoggerFactory.getLogger(BasicExecutable.class);

    /**
     * 工作空间
     */
    private WorkArea workArea;

    protected abstract void exec();

    /**
     * 待执行的子任务
     */
    protected List<Executable> subtasks;

    public void setWorkArea(WorkArea workArea) {
        this.workArea = workArea;
    }

    public void addTask(Executable executable) {
        if (subtasks == null) {
            subtasks = new ArrayList<>();
        }
        subtasks.add(executable);
    }

    public void addTasks(Collection<Executable> executables) {
        if (this.subtasks == null) {
            this.subtasks = new ArrayList<>(executables);
        } else {
            this.subtasks.addAll(executables);
        }
    }

    @Override
    public void run() {
        if (subtasks != null && !subtasks.isEmpty()) {
            exec();
        }
    }

    public WorkArea getWorkArea() {
        return workArea;
    }
}