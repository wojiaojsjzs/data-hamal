package com.striveonger.study.task.plugin;

import com.striveonger.study.task.common.executor.step.item.ItemWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-07-21 18:05
 */
public class ContextWriter extends Item implements ItemWriter<Map<String, Object>> {
    private final Logger log = LoggerFactory.getLogger(ContextWriter.class);

    @Override
    public void write(Map<String, Object> output) throws Exception {

    }
}