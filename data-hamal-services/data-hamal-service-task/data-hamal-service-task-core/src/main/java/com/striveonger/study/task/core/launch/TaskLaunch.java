package com.striveonger.study.task.core.launch;

import com.striveonger.study.task.core.constant.TaskStatus;
import com.striveonger.study.task.core.exception.BuildTaskException;
import com.striveonger.study.task.core.executor.Executor;
import com.striveonger.study.task.core.executor.assembly.ExecutorAssembly;
import com.striveonger.study.task.core.executor.extra.ExecutorExtraInfo;
import com.striveonger.study.task.core.executor.assembly.graph.Adapter;
import com.striveonger.study.task.core.executor.assembly.graph.Graph;
import com.striveonger.study.task.core.listener.Listener;
import com.striveonger.study.task.core.listener.step.StepExecuteTimerListener;
import com.striveonger.study.task.core.listener.step.StepLogListener;
import com.striveonger.study.task.core.scope.Workbench;
import com.striveonger.study.task.core.scope.context.RuntimeContext;
import com.striveonger.study.task.core.scope.trigger.TaskTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import com.striveonger.study.task.core.exception.BuildTaskException.Type;

/**
 * @author Mr.Lee
 * @description: Task启动器
 * @date 2023-04-28 16:13
 */
public class TaskLaunch {
    private final Logger log = LoggerFactory.getLogger(TaskLaunch.class);

    // TODO: 后面可以考虑把 launch包, 移入 data-hamal-service-task-executor 模块中

    private final TaskTrigger trigger;

    public TaskLaunch(TaskTrigger trigger) {
        this.trigger = trigger;
    }

    /**
     * 启动任务
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
        RuntimeContext cxt = new RuntimeContext(trigger);

        // 2. 初始化listener (todo: 临时手动new, 后面考虑采用ServiceLoader的方式来加载listener)
        Listener[] listeners = new Listener[] {new StepExecuteTimerListener(), new StepLogListener()};

        // 3. 初始化工作空间
        Workbench workbench = Workbench.builder().taskID(trigger.getTaskID()).corePoolSize(1).maximumPoolSize(64).context(cxt).build();

        // 4. 初始化 Executor (设置 "listener", "工作空间" )
        List<Executor> executors = trigger.getExtras().stream().map(ExecutorExtraInfo::getExecutor).toList();
        executors.forEach(executor -> {
            executor.setListeners(listeners);
            executor.setWorkbench(workbench);
        });

        // 5. 生成 FlowExecutor
        Map<String, Set<String>> topology = trigger.getTopology();
        Map<Executor, Set<Executor>> data = new HashMap<>();
        for (Map.Entry<String, Set<String>> entry : topology.entrySet()) {
            ExecutorExtraInfo key = trigger.getExtra(entry.getKey());
            if (key == null) {
                throw new BuildTaskException(Type.STEP, "topology key not match executor...");
            }
            Set<Executor> value = new HashSet<>();
            for (String s : entry.getValue()) {
                ExecutorExtraInfo extra = trigger.getExtra(s);
                if (extra == null) {
                    throw new BuildTaskException(Type.STEP, "topology key not match executor...");
                }
                value.add(extra.getExecutor());
            }
            data.put(key.getExecutor(), value);
        }
        Executor master = ExecutorAssembly.Builder.builder().graph(data).workbench(workbench).build().assembly();
        // 6.

        // 7. 启动
        try {
            doBefore();
            master.execute();
            doAfter();
        } catch (Exception e) {
            doError();
            e.printStackTrace();
        }
        
        // 8.

        return TaskStatus.FAIL;
    }

    private void doBefore() {
        log.info("task start: {}", trigger.getTaskID());
    }

    private void doAfter() {

    }

    private void doError() {

    }

}
