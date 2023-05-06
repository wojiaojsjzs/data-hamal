package com.striveonger.study.task.core.launch;

import com.striveonger.study.task.core.constant.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Mr.Lee
 * @description: Task启动器
 * @date 2023-04-28 16:13
 */
public class TaskLaunch {
    private final Logger log = LoggerFactory.getLogger(TaskLaunch.class);

    // TODO: 后面可以考虑把 launch包, 移入 data-hamal-service-task-executor 模块中

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

        return null;
    }



}
