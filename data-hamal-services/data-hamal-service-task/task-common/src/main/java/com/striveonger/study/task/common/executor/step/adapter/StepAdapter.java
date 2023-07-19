package com.striveonger.study.task.common.executor.step.adapter;

import com.striveonger.study.task.common.executor.step.item.ItemProcessor;
import com.striveonger.study.task.common.executor.step.item.ItemReader;
import com.striveonger.study.task.common.executor.step.item.ItemWriter;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-07-19 11:25
 */
public interface StepAdapter<T> {

    public default ItemReader<T> reader() {
        // TODO: ContextReader
        return null;
    }

    public default ItemProcessor<T, T> processor() {
        // NonProcessor 就没有处理, 原样输出
        return x -> x;
    }

    public default ItemWriter<T> writer() {
        // TODO: ContextWriter
        return null;
    }

}