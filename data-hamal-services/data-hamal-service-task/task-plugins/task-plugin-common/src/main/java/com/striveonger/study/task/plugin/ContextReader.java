package com.striveonger.study.task.plugin;

import com.striveonger.study.task.common.executor.step.item.ItemReader;
import com.striveonger.study.task.common.scope.context.StepContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-07-21 15:00
 */
public class ContextReader extends Item implements ItemReader<Map<String, Object>> {
    private final Logger log = LoggerFactory.getLogger(ContextReader.class);

    @Override
    public Map<String, Object> read() throws Exception {
        return null;
    }

}