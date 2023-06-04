package com.striveonger.study.task.core.scope.context;

import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import com.striveonger.study.task.common.scope.context.StepContext;
import com.striveonger.study.task.common.scope.context.TaskContext;
import com.striveonger.study.task.core.exception.BuildTaskException;
import com.striveonger.study.task.core.executor.Executable;
import com.striveonger.study.task.core.executor.Executor;
import com.striveonger.study.task.core.executor.extra.ExecutorExtraInfo;
import com.striveonger.study.task.core.scope.trigger.TaskTrigger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mr.Lee
 * @description: 运行时上下文对象
 * @date 2023-05-04 18:18
 */
public class RuntimeContext {

    private final TaskContext taskContext;

    private final Map<Executor, StepContext> stepContexts;

    public RuntimeContext(TaskTrigger trigger) {
        if (trigger == null) throw new CustomException(ResultStatus.TASK_EXECUTE_FAIL, "task trigger is null...");
        this.taskContext = new TaskContext(trigger.getTaskID(), trigger.getParams());
        this.stepContexts = new ConcurrentHashMap<>();
        for (ExecutorExtraInfo extra : trigger.getExtras()) {
            StepContext context = new StepContext();
            context.setTaskContext(this.taskContext);
            // todo: 后面有新的属性, 可以在这加...这也就是先打个样...
            context.setStepID(extra.getStepID());
            context.setDisplayName(extra.getDisplayName());
            this.stepContexts.put(extra.getExecutor(), context);
        }
    }

    public TaskContext getTaskContext() {
        return taskContext;
    }

    public StepContext getStepContext(Executor executor) {
        return stepContexts.get(executor);
    }

}
