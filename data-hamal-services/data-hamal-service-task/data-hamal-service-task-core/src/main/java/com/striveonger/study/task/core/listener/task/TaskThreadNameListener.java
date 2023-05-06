package com.striveonger.study.task.core.listener.task;

import com.striveonger.study.task.core.listener.ExecuteListener;
import com.striveonger.study.task.core.listener.Listener;
import com.striveonger.study.task.core.scope.context.RuntimeContext;
import com.striveonger.study.task.core.scope.context.StepContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-04-24 10:01
 */
@ExecuteListener(type = Listener.Type.STEP, order = -1)
public class TaskThreadNameListener implements Listener {

    private String tempName;

    @Override
    public void before(StepContext context) {

    }

    @Override
    public void after(StepContext context) {

    }

    @Override
    public void error(StepContext context) {

    }
}
