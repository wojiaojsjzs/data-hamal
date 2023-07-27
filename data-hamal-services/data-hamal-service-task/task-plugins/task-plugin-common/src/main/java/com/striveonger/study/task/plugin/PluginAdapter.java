package com.striveonger.study.task.plugin;

import com.striveonger.study.task.common.executor.step.adapter.StepAdapter;
import com.striveonger.study.task.common.executor.step.item.ItemProcessor;
import com.striveonger.study.task.common.executor.step.item.ItemReader;
import com.striveonger.study.task.common.executor.step.item.ItemWriter;
import com.striveonger.study.task.common.scope.context.StepContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-07-21 17:37
 */
public class PluginAdapter implements StepAdapter<Map<String, Object>, Map<String, Object>> {
    private final Logger log = LoggerFactory.getLogger(PluginAdapter.class);

    @Override
    public ItemReader<Map<String, Object>> reader() {
        return new ContextReader();
    }

    @Override
    public ItemProcessor<Map<String, Object>, Map<String, Object>> processor() {
        return new NonProcessor();
    }

    @Override
    public ItemWriter<Map<String, Object>> writer() {
        return new ContextWriter();
    }
}