package com.striveonger.study.task.core.executor;

import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import com.striveonger.study.task.core.listener.Listener;
import com.striveonger.study.task.core.scope.Workbench;
import com.striveonger.study.task.core.scope.context.RuntimeContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

    private Listener listener = null;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public Executor() { }

    public void setWorkbench(Workbench workbench) {
        this.workbench = workbench;
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
        if (Objects.nonNull(listener)) {
            listener.before(workbench.getContext());
        }
    }

    private void doAfter() {
        if (Objects.nonNull(listener)) {
            listener.after(workbench.getContext());
        }
    }

    private void doError() {
        if (Objects.nonNull(listener)) {
            listener.error(workbench.getContext());
        }
    }


}