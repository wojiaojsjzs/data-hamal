package com.striveonger.study.task.core.flow;

import com.striveonger.study.task.core.Executable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mr.Lee
 * @description: 串行执行器
 * @date 2022-11-28 20:50
 */
public class SerialFlow extends BasicExecutable {

    private final Logger log = LoggerFactory.getLogger(SerialFlow.class);

    @Override
    public void exec() {
        for (Executable task : this.subtasks) {
            // TODO: 执行前要前检查任务状态
            try {
                task.run();
            } finally {
                // 如果失败就要更新整个任务的执行状态

            }
        }
    }
}