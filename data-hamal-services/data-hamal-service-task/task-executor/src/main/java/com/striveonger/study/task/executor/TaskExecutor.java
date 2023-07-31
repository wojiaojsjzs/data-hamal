package com.striveonger.study.task.executor;

import com.striveonger.study.task.common.constant.TaskStatus;
import com.striveonger.study.task.common.scope.context.storage.ContextStorage;
import com.striveonger.study.task.core.launch.TaskLaunch;
import com.striveonger.study.task.core.scope.context.storage.ContextStorageHolder;
import com.striveonger.study.task.core.scope.trigger.TaskTrigger;
import com.striveonger.study.task.executor.adapter.AdapterConverter;
import com.striveonger.study.task.executor.entity.TaskBody;

/**
 * @author Mr.Lee
 * @description: 任务执行器
 * @date 2023-07-14 11:35
 */
public class TaskExecutor {

    /**
     * 执行任务
     * @param body
     * @return
     */
    public TaskStatus exec(TaskBody body) {
        // 生成任务触发器
        TaskTrigger trigger = new TaskTrigger();
        // 1. 任务ID
        trigger.setTaskID(body.getId());
        // 2. 任务执行参数
        trigger.putAllParam(body.getParams());
        // 3. 根据组件适配器生成Executor及其扩展信息
        trigger.putAllExtra(AdapterConverter.transform(body.getSteps()));

        // 4. 任务的执行顺序
        trigger.setTopology(body.getTopology());
        // 5. 任务Context的存储器
        ContextStorage storage = ContextStorageHolder.getInstance().getStorage();
        trigger.setStorage(storage);
        // 6. 启动任务
        TaskLaunch launch = new TaskLaunch(trigger);
        return launch.start();
    }
}
