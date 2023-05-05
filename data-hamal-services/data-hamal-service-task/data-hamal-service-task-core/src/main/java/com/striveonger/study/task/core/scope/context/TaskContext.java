package com.striveonger.study.task.core.scope.context;

import com.striveonger.study.task.core.scope.trigger.PerformParam;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mr.Lee
 * @description: Task运行时环境
 * @date 2023-05-05 22:35
 */
public class TaskContext {
    private long taskID;
    private Map<String, PerformParam> param = new ConcurrentHashMap<>();

}
