package com.striveonger.study.task.core.scope.context;

import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import com.striveonger.study.task.common.scope.context.StepContext;
import com.striveonger.study.task.common.scope.context.TaskContext;
import com.striveonger.study.task.core.executor.Executor;
import com.striveonger.study.task.core.executor.extra.ExecutorExtraInfo;
import com.striveonger.study.task.core.scope.trigger.TaskTrigger;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mr.Lee
 * @description: 运行时上下文对象
 * @date 2023-05-04 18:18
 */
public interface RuntimeContext {

    public TaskContext getTaskContext();

    public StepContext getStepContext(Executor executor);

    public void initialize(TaskTrigger trigger);

    /**
     * 默认使用"本地内存"存储运行时上下文对象
     */
    public static class Default implements RuntimeContext {

        private TaskContext taskContext;

        private Map<Executor, StepContext> stepContexts;

        @Override
        public TaskContext getTaskContext() {
            return taskContext;
        }

        @Override
        public StepContext getStepContext(Executor executor) {
            return stepContexts.get(executor);
        }

        @Override
        public void initialize(TaskTrigger trigger) {
            if (trigger == null) throw new CustomException(ResultStatus.TASK_EXECUTE_FAIL, "task trigger is null...");
            this.taskContext = new TaskContext(trigger.getTaskID(), trigger.getExtras().size(), trigger.getParams());
            this.stepContexts = new ConcurrentHashMap<>();
            int idx = 0;
            for (ExecutorExtraInfo extra : trigger.getExtras()) {
                StepContext context = new StepContext();
                // 给每个任务, 随机分配一个索引值(以对应 RuntimeStatus)
                context.setIndex(idx++);
                context.setTaskContext(this.taskContext);
                // todo: 后面有新的属性, 可以在这加...这也就是先打个样...
                context.setStepID(extra.getStepID());
                context.setDisplayName(extra.getDisplayName());
                this.stepContexts.put(extra.getExecutor(), context);
            }
        }
    }

    public static class Holder {
        private static final Holder instance = new Holder();

        private final RuntimeContext context;

        private Holder() {
            ServiceLoader<RuntimeContext> loader = ServiceLoader.load(RuntimeContext.class);
            // 尝试加载外部实现, 如果没有加载到外部实现类, 则使用默认的
            this.context = loader.findFirst().orElseGet(Default::new);
        }

        public static RuntimeContext getContext(TaskTrigger trigger) {
            instance.context.initialize(trigger);
            return instance.context;
        }
    }


}
