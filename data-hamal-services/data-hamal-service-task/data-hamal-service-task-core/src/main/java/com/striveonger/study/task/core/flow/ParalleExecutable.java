package com.striveonger.study.task.core.flow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mr.Lee
 * @description: 并行执行器
 * @date 2022-11-28 22:12
 */
public class ParalleExecutable extends BasicExecutable {
    private final Logger log = LoggerFactory.getLogger(ParalleExecutable.class);

    @Override
    public boolean exec() {
        if (subtasks == null || subtasks.isEmpty()) {
            return false;
        }

        return true;
    }
}