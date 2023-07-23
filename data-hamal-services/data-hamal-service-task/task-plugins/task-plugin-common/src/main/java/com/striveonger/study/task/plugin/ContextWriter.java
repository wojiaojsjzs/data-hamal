package com.striveonger.study.task.plugin;

import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import com.striveonger.study.task.common.executor.step.item.ItemWriter;
import com.striveonger.study.task.common.scope.context.StepContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author Mr.Lee
 * @description: 将数据写入Context
 * @date 2023-07-21 18:05
 */
public class ContextWriter extends Item implements ItemWriter<Map<String, Object>> {
    private final Logger log = LoggerFactory.getLogger(ContextWriter.class);

    @Override
    public void write(Map<String, Object> output) throws Exception {

    }

    @Override
    public void finish() throws Exception {

    }


    /**
     * 获取存储Key
     */
    public String getStorageKey() {
        StepContext context = getContext();
        if (context == null) throw new CustomException(ResultStatus.TASK_EXECUTE_FAIL, "Invalid Step Context...");
        String currentStepID = context.getStepID();



    }
}