package com.striveonger.study.task.core.scope.context;

import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import com.striveonger.study.task.core.scope.trigger.PerformParam;
import com.striveonger.study.task.core.scope.trigger.TaskTrigger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mr.Lee
 * @description: 运行时上下文对象
 * @date 2023-05-04 18:18
 */
public class RuntimeContext {

    private final TaskContext taskContext;

    private final StepContext[] stepContexts;

    public RuntimeContext(long taskID, int size, TaskTrigger trigger) {
        this.taskContext = new TaskContext();
        this.stepContexts = new StepContext[size];
    }

    public TaskContext getTaskContext() {
        return taskContext;
    }

    public StepContext getStepContext(int idx) {
        if (idx < 0 || idx >= stepContexts.length) {
            throw new CustomException(ResultStatus.ACCIDENT);
        }
        return stepContexts[idx];
    }

    /**
     * Task运行时环境
     */
    public static class TaskContext {
        private long taskID;
        private Map<String, PerformParam> param = new ConcurrentHashMap<>();

    }

    /**
     * Step运行时环境
     */
    public static class StepContext {
        private long stepID;
            private String displayName;
    }
}
