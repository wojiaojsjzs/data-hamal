package com.striveonger.study.task.common.scope.context;


import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.striveonger.study.core.exception.CustomException;
import com.striveonger.study.task.common.constant.StepStatus;
import com.striveonger.study.task.common.constant.TaskStatus;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.striveonger.study.core.constant.ResultStatus.NOT_FOUND;

/**
 * @author Mr.Lee
 * @description: Task运行时环境
 * @date 2023-05-05 22:35
 */
public class TaskContext {

    private final String taskID;

    private final Integer total;

    /**
     * 运行时上下文
     */
    private final RuntimeContext runtimeContext;

    private final Multimap<String, String> prev;

    private final Multimap<String, String> next;

    /**
     * 运行参数
     */
    private final Map<String, PerformParam> params;

    /**
     * 任务启动, 所需的资源
     * 比如: RedisHolder, TaskExecutorLogHolder, WebSocketHolder
     */
    private final Map<Class<?>, Object> resources;



    /**
     * 初始化任务的上下文对象
     * @param taskID    任务ID
     * @param prev      步骤前继节点的记录
     * @param next      步骤后细节点的记录
     * @param params    任务执行参数
     * @param resources 任务执行资源
     */
    public TaskContext(String taskID,
                       RuntimeContext runtimeContext,
                       Multimap<String, String> prev, Multimap<String, String> next,
                       List<PerformParam> params, List<Object> resources) {
        this.taskID = taskID;
        this.total = next.size();
        this.runtimeContext = runtimeContext;
        this.prev = prev; this.next = next;
        this.params = new ConcurrentHashMap<>();
        this.resources = new ConcurrentHashMap<>();
        for (PerformParam param : params) {
            this.params.put(param.getName(), param);
        }
        for (Object o : resources) {
            this.resources.put(o.getClass(), o);
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
            if (Objects.nonNull(param) && param.isAlterable()) {
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

    public Multimap<String, String> getPrev() {
        return prev;
    }

    public Multimap<String, String> getNext() {
        return next;
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

    public static final class Builder {
        private String taskID = null;
        private RuntimeContext runtimeContext = null;
        private Multimap<String, String> prev = null;
        private Multimap<String, String> next = null;
        private final List<PerformParam> params = new ArrayList<>();
        private final List<Object> resources = new ArrayList<>();

        public static Builder builder() {
            return new Builder();
        }

        public Builder taskID(String taskID) {
            this.taskID = taskID;
            return this;
        }

        public Builder runtimeContext(RuntimeContext context) {
            this.runtimeContext = context;
            return this;
        }

        public Builder topology(Map<String, Set<String>> topology) {
            // 初始化prev, next
            this.prev = HashMultimap.create();
            this.next = HashMultimap.create();
            for (String from : topology.keySet()) {
                Set<String> set = topology.get(from);
                for (String to : set) {
                    next.put(from, to);
                    prev.put(to, from);
                }
            }
            return this;
        }

        public Builder params(List<PerformParam> params) {
            this.params.clear();
            this.params.addAll(params);
            return this;
        }

        public Builder resources(List<Object> resources) {
            this.resources.clear();
            this.resources.addAll(resources);
            return this;
        }

        public TaskContext build() {
            if (taskID == null || runtimeContext == null
                               || prev == null || next == null) {
                throw new CustomException(NOT_FOUND, "Build Task Context Missing values...");
            }
            return new TaskContext(taskID, runtimeContext, prev, next, params, resources);
        }
    }




}
