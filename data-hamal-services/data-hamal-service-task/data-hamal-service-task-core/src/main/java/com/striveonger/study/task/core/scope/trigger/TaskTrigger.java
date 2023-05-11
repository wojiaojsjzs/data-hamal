package com.striveonger.study.task.core.scope.trigger;

import com.striveonger.study.task.core.executor.extra.ExecutorExtraInfo;
import com.striveonger.study.task.core.listener.Listener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mr.Lee
 * @description: 任务触发器
 * @date 2023-05-04 15:35
 */
public class TaskTrigger {
    /**
     * taskID
     */
    private long taskID;

    /**
     * task 执行参数
     */
    private final List<PerformParam> params = new ArrayList<>();

    /**
     * task 执行列表 (包含扩展信息)
     */
    private final List<ExecutorExtraInfo> extras = new ArrayList<>();

    /**
     * task 执行监听
     */
    private final List<Listener> listeners = new ArrayList<>();

    /**
     * task 执行结构 (用邻接表表示) TODO: 后面再定义结构吧...
     */
    // private List<Object> adjacencyList;


    public long getTaskID() {
        return taskID;
    }

    public void setTaskID(long taskID) {
        this.taskID = taskID;
    }

    public List<PerformParam> getParams() {
        return params;
    }

    public void setParams(List<PerformParam> params) {
        this.params.addAll(params);
    }

    public void setParam(PerformParam param) {
        params.add(param);
    }

    public List<ExecutorExtraInfo> getExtras() {
        return extras;
    }

    public void setExtras(List<ExecutorExtraInfo> extras) {
        this.extras.addAll(extras);
    }

    public void setExtra(ExecutorExtraInfo extra) {
        this.extras.add(extra);
    }

    public List<Listener> getListeners() {
        return listeners;
    }

    public void setListeners(List<Listener> listeners) {
        this.listeners.addAll(listeners);
    }

    public void setListener(Listener listener) {
        this.listeners.add(listener);
    }



}
