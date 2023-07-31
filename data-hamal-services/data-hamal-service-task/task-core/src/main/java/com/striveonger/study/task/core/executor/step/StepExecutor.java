package com.striveonger.study.task.core.executor.step;

import com.striveonger.study.task.common.executor.step.item.ItemProcessor;
import com.striveonger.study.task.common.executor.step.item.ItemReader;
import com.striveonger.study.task.common.executor.step.item.ItemWriter;
import com.striveonger.study.task.common.scope.context.StepContext;
import com.striveonger.study.task.core.exception.BuildTaskException;
import com.striveonger.study.task.core.executor.Executor;
import com.striveonger.study.task.core.scope.Workbench;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.striveonger.study.task.core.exception.BuildTaskException.Type.STEP;

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
        if (reader == null || processor == null || writer == null) {
            throw new BuildTaskException(STEP, "Build step contain item is null...");
        }
        this.reader = reader;
        this.processor = processor;
        this.writer = writer;
    }

    @Override
    public void execute() throws Exception {
        I in;
        while ((in = reader.read()) != null) {
            O out = processor.process(in);
            writer.write(out);
        }
        writer.finish();
    }

    @Override
    public void setWorkbench(Workbench workbench) {
        super.setWorkbench(workbench);
        // 初始化 Items
        StepContext context = workbench.getStepContext(this);
        reader.setContext(context);
        processor.setContext(context);
        writer.setContext(context);
    }
}
