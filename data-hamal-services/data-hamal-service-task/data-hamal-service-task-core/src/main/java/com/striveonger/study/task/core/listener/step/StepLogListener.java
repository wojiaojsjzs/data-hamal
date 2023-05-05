package com.striveonger.study.task.core.listener.step;

import com.striveonger.study.task.core.listener.ExecuteListener;
import com.striveonger.study.task.core.listener.Listener;
import com.striveonger.study.task.core.scope.context.RuntimeContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-04-24 10:01
 */
@ExecuteListener(type = Listener.Type.STEP)
public class StepLogListener extends Listener {
    private final Logger log = LoggerFactory.getLogger(StepLogListener.class);

    private Listener next = null;



    @Override
    protected void execBefore(RuntimeContext context) {

    }

    @Override
    protected void execAfter(RuntimeContext context) {

    }

    @Override
    protected void execError(RuntimeContext context) {
        log.info("Step execute start");
    }
    // @Override
    public void before(RuntimeContext context) {
        log.info("Step execute start");
    }

    // @Override
    public void after(RuntimeContext context) {
        log.info("Step execute finish");
    }

    // @Override
    public void error(RuntimeContext context) {
        log.info("Step execute error");
    }

    public void setNext(Listener next) {
        this.next = next;
    }

    @Override
    public Listener getNext() {
        return next;
    }
}
