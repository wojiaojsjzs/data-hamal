package com.striveonger.study.task.core.executor;

import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import com.striveonger.study.task.common.StepListener;
import com.striveonger.study.task.common.scope.context.StepContext;
import com.striveonger.study.task.core.scope.Workbench;
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

    private StepListener[] listeners = null;

    public Executor() { }

    public void setWorkbench(Workbench workbench) {
        this.workbench = workbench;
    }

    public void setListeners(StepListener[] listeners) {
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
            doError(e);
            log.error("task execute occur error...", e);
            if (e instanceof CustomException ce) {
                throw ce;
            }
            throw new CustomException(ResultStatus.TASK_EXECUTE_FAIL, e.getMessage());
        }
    }

    private void doBefore() {
        if (Objects.nonNull(listeners)) {
            // 顺序执行
            for (int i = 0; i < listeners.length; i++) {
                listeners[i].before(getStepContext());
            }
        }
    }

    private void doAfter() {
        if (Objects.nonNull(listeners)) {
            // 逆序执行
            for (int i = listeners.length - 1; i >= 0; i--) {
                listeners[i].after(getStepContext());
            }
        }
    }

    private void doError(Exception e) {
        if (Objects.nonNull(listeners)) {
            // 逆序执行
            for (int i = listeners.length - 1; i >= 0; i--) {
                listeners[i].error(getStepContext(), e);
            }
        }
    }


}