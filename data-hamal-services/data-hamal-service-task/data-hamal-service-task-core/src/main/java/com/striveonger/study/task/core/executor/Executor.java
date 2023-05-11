package com.striveonger.study.task.core.executor;

import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import com.striveonger.study.task.core.listener.Listener;
import com.striveonger.study.task.core.scope.Workbench;
import com.striveonger.study.task.core.scope.context.StepContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * @author Mr.Lee
 * @description: 基础的执行器
 * @date 2022-11-28 21:23
 */
public abstract class Executor implements Executable {
    private final Logger log = LoggerFactory.getLogger(Executor.class);

    /**
     * 工作空间
     */
    protected Workbench workbench;

    private Listener[] listeners = null;

    public Executor() { }

    public void setWorkbench(Workbench workbench) {
        this.workbench = workbench;
    }

    public void setListeners(Listener[] listeners) {
        this.listeners = listeners;
    }

    public StepContext getStepContext() {
        return workbench.getContext().getStepContext(this);
    }

    @Override
    public final void run() {
        try {
            doBefore();
            execute();
            doAfter();
        } catch (Exception e) {
            doError();
            log.error("task execute occur error...", e);
            if (e instanceof CustomException ce) {
                throw ce;
            }
            throw new CustomException(ResultStatus.TASK_EXECUTE_FAIL);
        }
    }

    private void doBefore() {
        if (Objects.nonNull(listeners)) {
            // TODO: 压栈&执行
            // listener.before(workbench.getContext());
        }
    }

    private void doAfter() {
        if (Objects.nonNull(listeners)) {
            // TODO: 弹栈&执行
            // listener.after(workbench.getContext());
        }
    }

    private void doError() {
        if (Objects.nonNull(listeners)) {
            // listener.error(workbench.getContext());
        }
    }


}