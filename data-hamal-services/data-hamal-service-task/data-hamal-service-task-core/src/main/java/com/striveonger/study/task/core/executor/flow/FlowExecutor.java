package com.striveonger.study.task.core.executor.flow;

import com.striveonger.study.task.core.executor.Executable;
import com.striveonger.study.task.core.executor.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Mr.Lee
 * @description: 流程控制执行器
 * @date 2022-11-28 21:23
 */
public abstract class FlowExecutor extends Executor {
    private final Logger log = LoggerFactory.getLogger(FlowExecutor.class);

    /**
     * 子任务
     */
    protected List<Executable> subtasks;

    public FlowExecutor() { }

    public final <T extends Executable> void push(T task) {
        if (subtasks == null) {
            subtasks = new ArrayList<>();
        }
        subtasks.add(task);
    }

    public final void push(Collection<? extends Executable> tasks) {
        if (this.subtasks == null) {
            this.subtasks = new ArrayList<>(tasks);
        } else {
            this.subtasks.addAll(tasks);
        }
    }
}