package com.striveonger.study.task.common.executor.step.adapter;

import com.striveonger.study.task.common.entity.BuildConfig;
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

    ItemReader<I> reader(BuildConfig config);

    ItemProcessor<I, O> processor(BuildConfig config);

    ItemWriter<I> writer(BuildConfig config);

    String type();
}