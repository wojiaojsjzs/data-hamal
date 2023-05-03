package com.striveonger.study.task.core.executor.step.item;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-04-24 11:01
 */
public interface ItemReader<I> {

    I read() throws Exception;
}
