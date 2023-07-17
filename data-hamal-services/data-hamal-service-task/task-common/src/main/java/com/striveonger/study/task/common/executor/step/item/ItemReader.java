package com.striveonger.study.task.common.executor.step.item;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-04-24 11:01
 */
public interface ItemReader<I> extends Item {

    I read() throws Exception;
}
