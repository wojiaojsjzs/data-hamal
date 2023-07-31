package com.striveonger.study.task.plugin;

import com.striveonger.study.task.common.entity.BuildConfig;
import com.striveonger.study.task.common.executor.step.item.ItemWriter;
import com.striveonger.study.task.plugin.entity.TableOutputBuildConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-07-30 15:16
 */
public class TableOutputStepAdapter extends PluginAdapter {
    private final Logger log = LoggerFactory.getLogger(TableOutputStepAdapter.class);

    @Override
    public ItemWriter<Map<String, Object>> writer(BuildConfig config) {
        if (config instanceof TableOutputBuildConfig c) {
            return new TableOutputWriter(c);
        }
        return null;
    }

    @Override
    public String type() {
        return "TABLE_OUTPUT";
    }
}
