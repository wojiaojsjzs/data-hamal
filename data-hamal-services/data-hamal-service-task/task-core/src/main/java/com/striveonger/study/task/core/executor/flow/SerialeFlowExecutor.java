package com.striveonger.study.task.core.executor.flow;

import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import com.striveonger.study.task.core.executor.Executable;
import com.striveonger.study.task.core.scope.Workbench;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mr.Lee
 * @description: 串行执行器
 * @date 2022-11-28 20:50
 */
public class SerialeFlowExecutor extends FlowExecutor {
    private final Logger log = LoggerFactory.getLogger(SerialeFlowExecutor.class);

    public SerialeFlowExecutor(Workbench workbench) {
        super(workbench);
    }

    @Override
    public void execute() throws Exception {
        for (Executable task : this.subtasks) {
            try {
                task.run();
            } catch (Exception e) {
                log.error("Seriale Executor execute failure...", e);
                if (e instanceof CustomException) throw e;
                throw new CustomException(ResultStatus.TASK_EXECUTE_FAIL);
            }
        }
    }
}