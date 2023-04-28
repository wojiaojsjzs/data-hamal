package com.striveonger.study.task.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-04-24 10:01
 */
@ExecuteListener
public class StepLogListener implements Listener {
    private final Logger log = LoggerFactory.getLogger(StepLogListener.class);

    private Listener next = null;

    @Override
    public void before() {
        log.info("Step execute start");
    }

    @Override
    public void after() {
        log.info("Step execute finish");
    }

    @Override
    public void error() {
        log.info("Step execute error");
    }

    public void setNext(Listener next) {
        this.next = next;
    }

    @Override
    public Listener next() {
        return next;
    }


}
