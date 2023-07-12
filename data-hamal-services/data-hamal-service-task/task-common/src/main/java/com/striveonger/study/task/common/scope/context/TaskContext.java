package com.striveonger.study.task.common.scope.context;


import com.striveonger.study.task.common.scope.status.StatusControls;

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
    private final Map<String, PerformParam> params = new ConcurrentHashMap<>();

    /**
     * 任务启动, 所需的资源
     * 比如: RedisHolder, TaskExecutorLogHolder, WebSocketHolder
     */
    private final Map<Class<?>, Object> resource = new ConcurrentHashMap<>();

    private final StatusControls controls;

    public TaskContext(long taskID, List<PerformParam> params) {
        this.taskID = String.valueOf(taskID);
        for (PerformParam param : params) {
            this.params.put(param.getName(), param);
        }
        controls = StatusControls.Holder.getControls();
    }

    public String getTaskID() {
        return taskID;
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

    public void putResource(Class<?> key, Object val) {
        if (Objects.nonNull(key) && Objects.nonNull(val)) {
            resource.put(key, val);
        }
    }

    public <T> T getResource(Class<T> key) {
        Object val = resource.get(key);
        if (Objects.nonNull(key) && Objects.nonNull(val) && key.isInstance(val)) {
            return (T) val;
        }
        return null;
    }


    public StatusControls getStatusControls() {
        return controls;
    }
}
