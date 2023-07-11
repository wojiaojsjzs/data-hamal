package com.striveonger.study.task.core.listener.step;

import com.striveonger.study.task.common.Listener;
import com.striveonger.study.task.common.scope.context.StepContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mr.Lee
 * @description: 执行状态监听器
 * @date 2023-05-16 17:01
 */
public class StepExecuteStatusListener implements Listener {
    private final Logger log = LoggerFactory.getLogger(StepExecuteStatusListener.class);

    @Override
    public void before(StepContext context) {

    }

    @Override
    public void after(StepContext context) {

        // log.info("Step '{}' elapsed time: {}ms", context.getDisplayName(), time);
    }

    @Override
    public void error(StepContext context) {

        // log.info("Step '{}' elapsed time: {}ms", context.getDisplayName(), time);
    }

    @Override
    public boolean need(StepContext context) {
        // 全部组件都需要添加的监听器
        return true;
    }
}
