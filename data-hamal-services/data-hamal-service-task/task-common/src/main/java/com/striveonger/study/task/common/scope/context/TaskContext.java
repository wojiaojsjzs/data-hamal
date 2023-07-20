package com.striveonger.study.task.common.scope.context;


import com.striveonger.study.task.common.constant.StepStatus;
import com.striveonger.study.task.common.constant.TaskStatus;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mr.Lee
 * @description: Task运行时环境
 * @date 2023-05-05 22:35
 */
public class TaskContext {
    private final String taskID;

    private final Integer total;

    private final Map<String, PerformParam> params = new ConcurrentHashMap<>();

    /**
     * 任务启动, 所需的资源
     * 比如: RedisHolder, TaskExecutorLogHolder, WebSocketHolder
     */
    private final Map<Class<?>, Object> resources = new ConcurrentHashMap<>();

    private final RuntimeContext runtimeContext;

    /**
     * 初始化任务的上下文对象
     * @param taskID   任务ID
     * @param total    步骤的总数
     * @param params   任务执行参数
     * @param rs       任务执行资源
     */
    public TaskContext(String taskID, int total, RuntimeContext runtimeContext, List<PerformParam> params, List<Object> rs) {
        this.taskID = taskID;
        this.total = total;
        this.runtimeContext = runtimeContext;
        for (PerformParam param : params) {
            this.params.put(param.getName(), param);
        }
        for (Object o : rs) {
            resources.put(o.getClass(), o);
        }
    }

    public String getTaskID() {
        return taskID;
    }

    public Integer getTotal() {
        return total;
    }

    public String getParam(String key) {
        PerformParam param = params.get(key);
        if (Objects.nonNull(param)) {
            return param.getValue();
        }
        return null;
    }

    public void putParam(String key, String val) {
        synchronized (key.intern()) {
            PerformParam param = params.get(key);
            if (Objects.nonNull(param)) {
                param.setValue(val);
            }
        }
    }

    public <T> T getResource(Class<T> key) {
        Object val = resources.get(key);
        if (Objects.nonNull(key) && Objects.nonNull(val) && key.isInstance(val)) {
            return (T) val;
        }
        return null;
    }

    public RuntimeContext getRuntimeContext() {
        return runtimeContext;
    }

    /* RuntimeStatus Proxy Start */

    /**
     * 开始记录任务状态
     */
    public void startRecordStatus() {
        runtimeContext.status().start(taskID, total);
    }

    /**
     * 停止记录任务状态
     */
    public void stopRecordStatus() {
        runtimeContext.status().stop(taskID);
    }

    public void updateRuntimeStatus(int index, StepStatus status) {
        runtimeContext.status().update(taskID, index, status);
    }

    public TaskStatus getTaskStatus() {
        return runtimeContext.status().taskStatus(taskID);
    }

    public StepStatus getStepStatus(int index) {
        return runtimeContext.status().stepStatus(taskID, index);
    }
    /* RuntimeStatus Proxy End */


}
