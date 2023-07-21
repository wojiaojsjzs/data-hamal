package com.striveonger.study.task.plugin;

import com.striveonger.study.task.common.executor.step.item.ItemProcessor;

import java.util.Map;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-04-24 11:03
 */
public class NonProcessor extends Item implements ItemProcessor<Map<String, Object>, Map<String, Object>> {

    @Override
    public Map<String, Object> process(Map<String, Object> in) throws Exception {
        return in;
    }

}
