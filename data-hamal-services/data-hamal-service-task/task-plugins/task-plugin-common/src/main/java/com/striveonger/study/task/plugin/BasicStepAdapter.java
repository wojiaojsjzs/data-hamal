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
 * @date 2023-07-26 22:11
 */
public abstract class BasicStepAdapter implements StepAdapter<Map<String, Object>, Map<String, Object>> {
    private final Logger log = LoggerFactory.getLogger(BasicStepAdapter.class);

    private final StepContext context;

    public BasicStepAdapter(StepContext context) {
        this.context = context;
    }

    @Override
    public ItemReader<Map<String, Object>> reader() {
        ContextReader item = new ContextReader();
        item.setContext(context);
        return item;
    }

    @Override
    public ItemProcessor<Map<String, Object>, Map<String, Object>> processor() {
        NonProcessor item = new NonProcessor();
        item.setContext(context);
        return item;
    }

    @Override
    public ItemWriter<Map<String, Object>> writer() {
        ContextWriter item = new ContextWriter();
        item.setContext(context);
        return item;
    }
}
