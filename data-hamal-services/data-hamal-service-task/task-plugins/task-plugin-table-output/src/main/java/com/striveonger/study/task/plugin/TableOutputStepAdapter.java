package com.striveonger.study.task.plugin;

import com.striveonger.study.task.common.executor.step.item.ItemWriter;
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
    public ItemWriter<Map<String, Object>> writer() {
        return new TableOutputWriter();
    }

    @Override
    public String type() {
        return "TABLE_OUTPUT";
    }
}
