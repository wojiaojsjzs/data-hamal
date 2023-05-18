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
public class StepLogListener implements Listener {
    private final Logger log = LoggerFactory.getLogger(StepLogListener.class);

    @Override
    public void before(StepContext context) {
        log.info("Step '{}' execute start...", context.getDisplayName());
    }

    @Override
    public void after(StepContext context) {
        log.info("Step '{}' execute finish...", context.getDisplayName());
    }

    @Override
    public void error(StepContext context) {
        log.info("Step '{}' execute an error occurs...", context.getDisplayName());
    }
}
