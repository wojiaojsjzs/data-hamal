package com.striveonger.study.task.core.flow;

import com.striveonger.study.task.core.Executable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mr.Lee
 * @description: 串行执行器
 * @date 2022-11-28 20:50
 */
public class SerialExecutable extends BasicExecutable {
    private final Logger log = LoggerFactory.getLogger(SerialExecutable.class);

    @Override
    public boolean exec() {
        if (subtasks == null || subtasks.isEmpty()) {
            return false;
        }
        boolean flag = true;
        for (int i = 0; i < subtasks.size() && flag; i++) {
            Executable executable = subtasks.get(i);
            flag = executable.exec();
        }
        return flag;
    }
}