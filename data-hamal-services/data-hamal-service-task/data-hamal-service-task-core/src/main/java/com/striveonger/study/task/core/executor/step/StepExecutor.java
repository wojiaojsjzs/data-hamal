package com.striveonger.study.task.core.executor.step;

import com.striveonger.study.task.core.executor.Executor;
import com.striveonger.study.task.core.executor.step.item.ItemProcessor;
import com.striveonger.study.task.core.executor.step.item.ItemReader;
import com.striveonger.study.task.core.executor.step.item.ItemWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-04-23 11:23
 */
public class StepExecutor<I, O> extends Executor {
    private final Logger log = LoggerFactory.getLogger(StepExecutor.class);

    // private int chunk = 1; // TODO: 先不考虑分块执行(比如, A表toB表, 每1000行, 写一次盘)

    private final ItemReader<I> reader;

    private final ItemProcessor<I, O> processor;

    private final ItemWriter<O> writer;

    public StepExecutor(ItemReader<I> reader, ItemProcessor<I, O> processor, ItemWriter<O> writer) {
        this.reader = reader;
        this.processor = processor;
        this.writer = writer;
    }

    @Override
    public void execute() throws Exception {
        I in = reader.read();
        O out = processor.process(in);
        writer.write(out);
    }
}
