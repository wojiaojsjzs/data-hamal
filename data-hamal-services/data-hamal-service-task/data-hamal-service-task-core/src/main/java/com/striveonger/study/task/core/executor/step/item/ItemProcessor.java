package com.striveonger.study.task.core.executor.step.item;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-04-24 11:03
 */
public interface ItemProcessor<I, O> {

    O process(I input) throws Exception;

}
