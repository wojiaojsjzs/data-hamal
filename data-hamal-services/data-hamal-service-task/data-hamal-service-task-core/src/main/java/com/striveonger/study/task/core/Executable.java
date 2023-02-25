package com.striveonger.study.task.core;

/**
 * @author Mr.Lee
 * @description: 可执行的
 * @date 2022-11-28 20:33
 */
public interface Executable extends Runnable {

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
}
