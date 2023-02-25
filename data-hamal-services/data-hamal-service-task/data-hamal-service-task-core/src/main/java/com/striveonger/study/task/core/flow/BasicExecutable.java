package com.striveonger.study.task.core.flow;

import com.striveonger.study.task.core.Workbench;
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

    protected final String name;

    public BasicExecutable(String name) {
        this.name = name;
    }

    /**
     * 工作空间
     */
    protected Workbench workbench;

    protected abstract void exec();

    /**
     * 待执行的子任务
     */
    protected List<Executable> subtasks;

    public void setWorkbench(Workbench workbench) {
        this.workbench = workbench;
    }

    public final void push(Executable task) {
        if (subtasks == null) {
            subtasks = new ArrayList<>();
        }
        subtasks.add(task);
    }

    public final void push(Collection<Executable> tasks) {
        if (this.subtasks == null) {
            this.subtasks = new ArrayList<>(tasks);
        } else {
            this.subtasks.addAll(tasks);
        }
    }

    @Override
    public final void run() {
        before();
        if (subtasks != null && !subtasks.isEmpty()) {
            exec();
        }
        after();
    }

    @Override
    public void before() {

    }

    @Override
    public void after() {

    }
}