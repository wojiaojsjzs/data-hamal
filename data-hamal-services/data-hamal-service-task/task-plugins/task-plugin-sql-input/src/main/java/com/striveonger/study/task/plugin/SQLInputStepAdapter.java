package com.striveonger.study.task.plugin;

import com.striveonger.study.task.common.executor.step.adapter.StepAdapter;
import com.striveonger.study.task.common.executor.step.item.ItemProcessor;
import com.striveonger.study.task.common.executor.step.item.ItemReader;
import com.striveonger.study.task.common.executor.step.item.ItemWriter;
import com.striveonger.study.task.common.scope.context.StepContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-07-26 22:09
 */
public class SQLInputStepAdapter implements StepAdapter {
    private final Logger log = LoggerFactory.getLogger(SQLInputStepAdapter.class);



    @Override
    public ItemReader reader() {
        return null;
    }

    @Override
    public ItemProcessor processor() {
        return null;
    }

    @Override
    public ItemWriter writer() {
        return null;
    }

    @Override
    public void initialize(StepContext context) {

    }
}
