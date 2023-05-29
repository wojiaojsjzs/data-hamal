package com.striveonger.study.task.common.scope.context;


import com.striveonger.study.task.common.scope.trigger.PerformParam;

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
    private final long taskID;
    private final Map<String, PerformParam> params = new ConcurrentHashMap<>();

    public TaskContext(long taskID, List<PerformParam> params) {
        this.taskID = taskID;
        for (PerformParam param : params) {
            this.params.put(param.getName(), param);
        }
    }

    public long getTaskID() {
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
}
