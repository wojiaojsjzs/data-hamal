package com.striveonger.study.task.plugin;

import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import com.striveonger.study.task.common.executor.step.item.ItemReader;
import com.striveonger.study.task.common.scope.context.RuntimeContext;
import com.striveonger.study.task.common.scope.context.StepContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-07-21 15:00
 */
public class ContextReader extends BasicItem implements ItemReader<Map<String, Object>> {
    private final Logger log = LoggerFactory.getLogger(ContextReader.class);

    @Override
    public Map<String, Object> read() throws Exception {
        RuntimeContext context = getContext().getTaskContext().getRuntimeContext();
        String key = getStorageKey();
        return context.poll(key, Map.class);
    }

    /**
     * 获取存储Key
     * 后继节点, 可能不止一个嘛
     */
    public String getStorageKey() {
        StepContext context = getContext();
        if (context == null) throw new CustomException(ResultStatus.TASK_EXECUTE_FAIL, "Invalid Step Context...");
        String taskID = context.getTaskID();
        String currentStepID = context.getStepID();
        String key = null;
        for (String prev : context.prev()) {
            key = taskID + "_" + prev + "_" + currentStepID;
            // 只处理单一前置节点的数据
            break;
        }
        return key;
    }

}