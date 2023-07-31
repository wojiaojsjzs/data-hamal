package com.striveonger.study.task.executor.adapter;

import com.striveonger.study.task.common.entity.BuildConfig;
import com.striveonger.study.task.common.executor.step.adapter.StepAdapter;
import com.striveonger.study.task.common.executor.step.item.ItemProcessor;
import com.striveonger.study.task.common.executor.step.item.ItemReader;
import com.striveonger.study.task.common.executor.step.item.ItemWriter;
import com.striveonger.study.task.core.exception.BuildTaskException;
import com.striveonger.study.task.core.executor.extra.ExecutorExtraInfo;
import com.striveonger.study.task.core.executor.step.StepExecutor;
import com.striveonger.study.task.executor.entity.StepBuildInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-07-31 19:53
 */
public class AdapterConverter {
    private final Logger log = LoggerFactory.getLogger(AdapterConverter.class);

    public static List<ExecutorExtraInfo> transform(List<StepBuildInfo> list) {
        return list.stream().map(AdapterConverter::transform).toList();
    }

    private static ExecutorExtraInfo transform(StepBuildInfo info) {
        ExecutorExtraInfo extra = new ExecutorExtraInfo();
        extra.setStepID(info.getId());
        extra.setDisplayName(info.getDisplayName());
        String type = info.getType();
        extra.setType(type);
        BuildConfig config = info.getBuildConfig();
        StepAdapter<Map<String, Object>, Map<String, Object>> adapter = AdapterLoader.getInstance().getStepAdapter(type);
        if (adapter == null) {
            throw new BuildTaskException(BuildTaskException.Type.STEP, String.format("Not Found type [%s] contra StepAdapter...", type));
        }
        ItemReader<Map<String, Object>> reader = adapter.reader(config);
        ItemProcessor<Map<String, Object>, Map<String, Object>> processor = adapter.processor(config);
        ItemWriter<Map<String, Object>> writer = adapter.writer(config);
        StepExecutor<Map<String, Object>, Map<String, Object>> executor = new StepExecutor<>(reader, processor, writer);
        extra.setExecutor(executor);
        return extra;
    }
}