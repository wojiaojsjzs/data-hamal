package com.striveonger.study.task.core.listener.step;

import com.striveonger.study.task.core.listener.ExecuteListener;
import com.striveonger.study.task.core.listener.Listener;
import com.striveonger.study.task.core.scope.context.RuntimeContext;
import com.striveonger.study.task.core.scope.context.StepContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-04-24 10:01
 */
@ExecuteListener(type = Listener.Type.STEP)
public class StepLogListener extends Listener {
    private final Logger log = LoggerFactory.getLogger(StepLogListener.class);

    @Override
    protected void execBefore(StepContext context) {
        log.info("Step {} execute start", "#");
    }

    @Override
    protected void execAfter(RuntimeContext context) {
        log.info("Step {} execute finish", "#");
    }

    @Override
    protected void execError(RuntimeContext context) {
        log.info("Step {} execute error", "#");
    }
}
