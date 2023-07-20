package com.striveonger.study.task.core.listener.task;

import com.striveonger.study.task.common.listener.TaskListener;
import com.striveonger.study.task.common.scope.context.TaskContext;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-07-12 19:11
 */
public class TaskInitializeListener implements TaskListener {

    @Override
    public void before(TaskContext context) {
        context.startRecordStatus();
    }

    @Override
    public void after(TaskContext context) {
        context.stopRecordStatus();
    }

    @Override
    public void error(TaskContext context, Exception e) {
        context.stopRecordStatus();
    }

    @Override
    public Priority getPriority() {
        return Priority.MAX;
    }
}
