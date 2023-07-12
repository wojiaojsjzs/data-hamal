package com.striveonger.study.task.core.listener.step;

import com.striveonger.study.task.common.StepListener;
import com.striveonger.study.task.common.constant.StepStatus;
import com.striveonger.study.task.common.scope.context.StepContext;
import com.striveonger.study.task.common.scope.status.StatusControls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mr.Lee
 * @description: 执行状态监听器
 * @date 2023-05-16 17:01
 */
public class StepExecuteStatusListener implements StepListener {
    private final Logger log = LoggerFactory.getLogger(StepExecuteStatusListener.class);

    @Override
    public void before(StepContext context) {
        process(context, StepStatus.RUNNING);
    }

    @Override
    public void after(StepContext context) {
        process(context, StepStatus.COMPLETE);
    }

    @Override
    public void error(StepContext context) {
        process(context, StepStatus.FAIL);
    }

    private void process(StepContext context,  StepStatus status) {
        String id = context.getTaskID();
        int index = context.getIndex();
        StatusControls controls = context.getStatusControls();
        synchronized (id.intern()) {

            controls.update(id, index, status);
        }
    }

    @Override
    public boolean need(StepContext context) {
        // 全部组件都需要添加的监听器
        return true;
    }
}
