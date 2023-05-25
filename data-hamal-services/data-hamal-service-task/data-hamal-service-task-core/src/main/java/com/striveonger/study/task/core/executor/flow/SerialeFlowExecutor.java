package com.striveonger.study.task.core.executor.flow;

import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import com.striveonger.study.task.core.executor.Executable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mr.Lee
 * @description: 串行执行器
 * @date 2022-11-28 20:50
 */
public class SerialeFlowExecutor extends FlowExecutor {
    private final Logger log = LoggerFactory.getLogger(SerialeFlowExecutor.class);

    @Override
    public void execute() {
        for (Executable task : this.subtasks) {
            try {
                task.run();
            } catch (Exception e) {
                log.error("Seriale Executor execute failure...", e);
                throw new CustomException(ResultStatus.TASK_EXECUTE_FAIL);
            }
        }
    }
}