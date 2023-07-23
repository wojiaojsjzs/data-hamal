package com.striveonger.study.task.plugin;

import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import com.striveonger.study.task.common.scope.context.StepContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-07-21 10:08
 */
public abstract class Item {
    private final Logger log = LoggerFactory.getLogger(Item.class);

    private StepContext context;

    public void setContext(StepContext context) {
        this.context = context;
    }

    /**
     * 获取上下文对象
     */
    public StepContext getContext() {
        return context;
    }

}