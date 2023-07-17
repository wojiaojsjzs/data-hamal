package com.striveonger.study.task.common.executor.step.item;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-04-24 11:03
 */
public interface ItemProcessor<I, O> extends Item {

    O process(I input) throws Exception;
}
