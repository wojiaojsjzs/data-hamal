package com.striveonger.study.task.plugin;

import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import com.striveonger.study.task.common.executor.step.item.ItemWriter;
import com.striveonger.study.task.common.scope.context.RuntimeContext;
import com.striveonger.study.task.common.scope.context.StepContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Mr.Lee
 * @description: 将数据写入Context (默认向后写多份)
 * @date 2023-07-21 18:05
 */
public class ContextWriter extends Item implements ItemWriter<Map<String, Object>> {
    private final Logger log = LoggerFactory.getLogger(ContextWriter.class);

    @Override
    public void write(Map<String, Object> output) throws Exception {
        RuntimeContext context = getContext().getTaskContext().getRuntimeContext();
        Set<String> keys = getStorageKeys();
        for (String key : keys) {
            context.offer(key, output);
        }
    }

    @Override
    public void finish() throws Exception {
        // pass
    }

    /**
     * 获取存储Key
     * 后继节点, 可能不止一个嘛
     */
    public Set<String> getStorageKeys() {
        StepContext context = getContext();
        if (context == null) throw new CustomException(ResultStatus.TASK_EXECUTE_FAIL, "Invalid Step Context...");
        String taskID = context.getTaskID();
        String currentStepID = context.getStepID();
        Set<String> keys = new HashSet<>();
        for (String next : context.next()) {
            keys.add(taskID + "_" + currentStepID + "_" + next);
        }
        return keys;
    }
}