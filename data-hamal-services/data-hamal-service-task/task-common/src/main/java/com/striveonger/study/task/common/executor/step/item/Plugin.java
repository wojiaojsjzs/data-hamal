package com.striveonger.study.task.common.executor.step.item;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-07-17 18:16
 */
public interface Plugin {
    /**
     * 组件的注册方法
     */
    void register();


    String type();
}