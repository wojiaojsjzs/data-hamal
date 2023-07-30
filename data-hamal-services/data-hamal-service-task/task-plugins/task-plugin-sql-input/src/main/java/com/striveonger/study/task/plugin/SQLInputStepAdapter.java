package com.striveonger.study.task.plugin;

import com.striveonger.study.task.common.executor.step.item.ItemReader;
import com.striveonger.study.task.common.scope.context.StepContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-07-26 22:09
 */
public class SQLInputStepAdapter extends PluginAdapter {
    private final Logger log = LoggerFactory.getLogger(SQLInputStepAdapter.class);

    @Override
    public ItemReader<Map<String, Object>> reader() {
        return new SQLInputReader();
    }

    @Override
    public String type() {
        return "SQL_INPUT";
    }

}
