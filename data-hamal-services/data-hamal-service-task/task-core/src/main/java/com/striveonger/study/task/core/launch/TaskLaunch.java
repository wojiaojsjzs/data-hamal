package com.striveonger.study.task.core.launch;

import com.striveonger.study.task.common.constant.TaskStatus;
import com.striveonger.study.task.common.executor.Executable;
import com.striveonger.study.task.common.listener.TaskListener;
import com.striveonger.study.task.common.scope.context.RuntimeContext;
import com.striveonger.study.task.common.scope.context.StepContext;
import com.striveonger.study.task.common.scope.context.TaskContext;
import com.striveonger.study.task.core.executor.Executor;
import com.striveonger.study.task.core.executor.assembly.ExecutorAssembly;
import com.striveonger.study.task.core.executor.extra.ExecutorExtraInfo;
import com.striveonger.study.task.core.listener.loader.StepListenerLoader;
import com.striveonger.study.task.core.listener.loader.TaskListenerLoader;
import com.striveonger.study.task.core.scope.Workbench;
import com.striveonger.study.task.core.scope.trigger.TaskTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mr.Lee
 * @description: Task启动器
 * @date 2023-04-28 16:13
 */
public class TaskLaunch {
    private final Logger log = LoggerFactory.getLogger(TaskLaunch.class);

    // TODO: 后面可以考虑把 launch包, 移入 data-hamal-service-task-executor 模块中

    private final TaskTrigger trigger;

    private final RuntimeContext runtimeContext;

    private final TaskContext taskContext;

    private final TaskListener[] listeners;

    public TaskLaunch(TaskTrigger trigger) {
        this.trigger = trigger;
        this.runtimeContext = new RuntimeContext(trigger.getStorage());
        this.listeners = TaskListenerLoader.getInstance().getFullRegisterListeners();
        this.taskContext = TaskContext.Builder.builder().taskID(trigger.getTaskID())
                .runtimeContext(runtimeContext)
                .topology(trigger.getTopology())
                .params(trigger.getParams())
                .build();
    }

    /**
     * 启动任务
     *
     * @return 任务执行结果
     */
    public TaskStatus start() {
        // 1. 初始化运行时环境
        // 2. 初始化listener
        // 3. 初始化工作空间
        // 4. 初始化 StepExecutor(设置 "运行时环境", "listener", "工作空间" )
        // 5. 生成 FlowExecutor
        // 6. 启动 MasterExecutor

        // 1. 根据触发器, 创建上下文对象
        // RuntimeContext cxt = RuntimeContext.Holder.getContext(trigger);


        Map<Executable, StepContext> stepContexts = new ConcurrentHashMap<>();
        int idx = 0;
        for (ExecutorExtraInfo extra : trigger.getExtras()) {
            StepContext context = new StepContext(idx++);
            // 给每个任务, 随机分配一个索引值(以对应 RuntimeStatus)
            context.setTaskContext(taskContext);
            context.setStepID(extra.getStepID());
            context.setDisplayName(extra.getDisplayName());
            stepContexts.put(extra.getExecutor(), context);
        }


        // 2. 初始化listener (TODO: 临时手动new, 后面考虑采用ServiceLoader的方式来加载listener)
        // Listener[] listeners = new Listener[] {new StepExecuteTimerListener(), new StepLogListener()};
        // Listener[] listeners = new Listener[] {new StepExecuteTimerListener()};


        // 3. 初始化工作空间(todo: 后面可以把task其他的配置信息, 也放到触发器里)
        Workbench workbench = Workbench.builder().taskID(trigger.getTaskID()).corePoolSize(0).maximumPoolSize(6).context(runtimeContext, taskContext, stepContexts).build();

        // 4. 初始化 Executor (设置 "listener", "工作空间" )
        List<Executor> executors = trigger.getExtras().stream().map(ExecutorExtraInfo::getExecutor).toList();
        executors.forEach(executor -> {
            executor.setListeners(StepListenerLoader.getInstance().getListenersByConditions(stepContexts.get(executor)));
            executor.setWorkbench(workbench);
        });

        // 5. 生成 FlowExecutor
        Executor master = ExecutorAssembly.Builder.builder().topology(trigger.getTopology()).extras(trigger.getExtraMap()).workbench(workbench).build().assembly();

        // 6. 初始化数据库操作对象等基础工作... todo 看后面安排吧, 有可能放到 Workbench 中, 让操作统一

        // 7. 启动
        TaskStatus status;
        try {
            doBefore();
            master.execute();
            doAfter();
            status = TaskStatus.COMPLETE;
        } catch (Exception e) {
            doError(e);
            log.error("oops, task execute an error occurs...", e);
            status = TaskStatus.FAIL;
        }

        // 8. 更新状态, 释放资源...

        return status;
    }

    private String name = null;

    private void doBefore() {
        name = Thread.currentThread().getName();
        Thread.currentThread().setName(String.format("task-exec-%s-thread-master", trigger.getTaskID()));
        log.info("task start, taskID: {}", trigger.getTaskID());

        for (int i = 0; i < listeners.length; i++) {
            listeners[i].before(taskContext);
        }
    }

    private void doAfter() {
        Thread.currentThread().setName(name);
        for (int i = listeners.length - 1; i >= 0; i--) {
            listeners[i].after(taskContext);
        }
    }

    private void doError(Exception e) {
        Thread.currentThread().setName(name);
        Thread.currentThread().setName(name);
        for (int i = listeners.length - 1; i >= 0; i--) {
            listeners[i].error(taskContext, e);
        }
    }

}
