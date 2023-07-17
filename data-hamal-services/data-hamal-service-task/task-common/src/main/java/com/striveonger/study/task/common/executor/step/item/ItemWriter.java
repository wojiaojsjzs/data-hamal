package com.striveonger.study.task.common.executor.step.item;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-04-24 11:05
 */
public interface ItemWriter<O> extends Item {

    void write(O output) throws Exception;
}
