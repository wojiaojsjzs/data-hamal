package com.striveonger.study.task.common.executor.step.item;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-04-24 11:01
 */
@FunctionalInterface
public interface ItemReader<I> {

    I read() throws Exception;

}
