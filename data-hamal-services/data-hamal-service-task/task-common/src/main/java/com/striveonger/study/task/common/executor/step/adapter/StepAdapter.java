package com.striveonger.study.task.common.executor.step.adapter;

import com.striveonger.study.task.common.executor.step.item.ItemProcessor;
import com.striveonger.study.task.common.executor.step.item.ItemReader;
import com.striveonger.study.task.common.executor.step.item.ItemWriter;
import com.striveonger.study.task.common.scope.context.StepContext;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-07-19 11:25
 */
public interface StepAdapter<I, O> {

    ItemReader<I> reader();

    ItemProcessor<I, O> processor();

    ItemWriter<I> writer();

    String type();
}