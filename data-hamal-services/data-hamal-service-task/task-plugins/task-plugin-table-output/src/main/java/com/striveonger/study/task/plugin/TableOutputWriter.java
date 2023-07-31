package com.striveonger.study.task.plugin;

import com.striveonger.study.core.utils.JacksonUtils;
import com.striveonger.study.task.common.executor.step.item.ItemWriter;
import com.striveonger.study.task.plugin.entity.TableOutputBuildConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-07-17 15:34
 */
public class TableOutputWriter extends BasicItem implements ItemWriter<Map<String, Object>> {
    private final Logger log = LoggerFactory.getLogger(TableOutputWriter.class);


    public TableOutputWriter(TableOutputBuildConfig config) {

    }

    @Override
    public void write(Map<String, Object> output) throws Exception {
        log.info("Write data: {}",  JacksonUtils.toJSONString(output));
    }

    @Override
    public void finish() throws Exception {
        log.info("Batch write data to disk...");
    }
}