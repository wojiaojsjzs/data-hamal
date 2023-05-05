package com.striveonger.study.task.core.listener.task;

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
@ExecuteListener(type = Listener.Type.STEP, order = -1)
public class TaskThreadNameListener extends Listener {
    private final Logger log = LoggerFactory.getLogger(TaskThreadNameListener.class);

    private Listener next = null;

    private String tempName;

    public void setNext(Listener next) {
        this.next = next;
    }

    @Override
    protected void execBefore(RuntimeContext context) {
        this.tempName = Thread.currentThread().getName();
    }

    @Override
    protected void execAfter(RuntimeContext context) {
        Thread.currentThread().setName(tempName);
    }

    @Override
    protected void execError(RuntimeContext context) {
        Thread.currentThread().setName(tempName);
    }

    @Override
    public Listener getNext() {
        return next;
    }
}
