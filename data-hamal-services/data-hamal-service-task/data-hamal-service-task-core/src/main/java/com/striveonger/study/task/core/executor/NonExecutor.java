package com.striveonger.study.task.core.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-05-24 18:16
 */
public class NonExecutor extends Executor {
    private final Logger log = LoggerFactory.getLogger(NonExecutor.class);

    @Override
    public void execute() throws Exception {

    }
}
