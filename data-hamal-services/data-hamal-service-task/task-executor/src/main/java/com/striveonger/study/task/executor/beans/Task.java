package com.striveonger.study.task.executor.beans;

import com.striveonger.study.task.common.scope.context.PerformParam;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Mr.Lee
 * @description: 任务实体
 * @date 2023-07-14 14:35
 */
public class Task {

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
    private List<Step> steps;

    /**
     * 执行顺序(DAG.Topology)
     */
    private Map<String, Set<String>> topology;


}
