package com.striveonger.study.task.core.listener;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-04-23 16:11
 */
public interface Listener {

    /*
     * TODO: 现在呢想到的
     *  执行前:
     *    1. 初始化执行环境
     *    2. 检查Task执行状态
     *  执行后:
     *    1. 更新Step状态
     *    2. 回收资源
     *  执行出错:
     *    1. 更新Step状态
     *    2. 终止Task运行
     */

    /**
     * 执行前
     */
    default void before() { }

    /**
     * 执行后
     */
    default void after() { }

    /**
     * 执行出错
     */
    default void error() { }

    Listener next();

    enum Type { ALL, TASK, STEP; }
}
