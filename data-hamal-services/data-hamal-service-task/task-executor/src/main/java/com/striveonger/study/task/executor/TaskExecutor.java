package com.striveonger.study.task.executor;

import com.striveonger.study.task.common.constant.TaskStatus;
import com.striveonger.study.task.core.launch.TaskLaunch;
import com.striveonger.study.task.core.scope.trigger.TaskTrigger;
import com.striveonger.study.task.executor.beans.Task;

/**
 * @author Mr.Lee
 * @description: 任务执行器
 * @date 2023-07-14 11:35
 */
public class TaskExecutor {

    /**
     * 执行任务
     * @param task
     * @return
     */
    public TaskStatus execute(Task task) {
        // 将Task任务解析为任务触发器
        TaskTrigger trigger = new TaskTrigger();


        // 2. 调用
        TaskLaunch launch = new TaskLaunch(trigger);
        return launch.start();
    }
}
