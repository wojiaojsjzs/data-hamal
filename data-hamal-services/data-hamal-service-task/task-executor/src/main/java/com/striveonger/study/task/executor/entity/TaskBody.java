package com.striveonger.study.task.executor.entity;

import com.striveonger.study.task.common.scope.context.PerformParam;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Mr.Lee
 * @description: 任务实体
 * @date 2023-07-14 14:35
 */
public class TaskBody {

    /**
     * 任务ID
     */
    private String id;

    /**
     * 执行参数
     */
    private List<PerformParam> params;

    /**
     * 执行步骤
     */
    private List<StepBuildInfo> steps;

    /**
     * 执行顺序(DAG.Topology)
     */
    private Map<String, Set<String>> topology;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<PerformParam> getParams() {
        return params;
    }

    public void setParams(List<PerformParam> params) {
        this.params = params;
    }

    public List<StepBuildInfo> getSteps() {
        return steps;
    }

    public void setSteps(List<StepBuildInfo> steps) {
        this.steps = steps;
    }

    public Map<String, Set<String>> getTopology() {
        return topology;
    }

    public void setTopology(Map<String, Set<String>> topology) {
        this.topology = topology;
    }
}
