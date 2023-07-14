package com.striveonger.study.task.core.listener.task;

import com.striveonger.study.task.common.TaskListener;
import com.striveonger.study.task.common.scope.context.TaskContext;
import com.striveonger.study.task.common.scope.status.StatusControls;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-07-12 19:11
 */
public class TaskInitializeListener implements TaskListener {

    @Override
    public void before(TaskContext context) {
        StatusControls controls = StatusControls.Holder.getControls();
        controls.start(context.getTaskID(), context.getTotal());
    }

    @Override
    public void after(TaskContext context) {
        StatusControls controls = StatusControls.Holder.getControls();
        controls.stop(context.getTaskID());
    }

    @Override
    public void error(TaskContext context, Exception e) {
        StatusControls controls = StatusControls.Holder.getControls();
        controls.stop(context.getTaskID());
    }

    @Override
    public Priority getPriority() {
        return Priority.MAX;
    }
}
