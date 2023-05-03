package com.striveonger.study.task.core.launch;

import com.striveonger.study.task.core.constant.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Mr.Lee
 * @description: Task启动器
 * @date 2023-04-28 16:13
 */
public class TaskLaunch {
    private final Logger log = LoggerFactory.getLogger(TaskLaunch.class);

    /**
     * 启动任务
     * @return 任务执行结果
     */
    public TaskStatus start() {
        return null;
    }


    public static class Trigger {
        private long taskID;
        private List<PerformParam> params;
    }


    public static class PerformParam {
        private String name;
        private String value;
        private boolean alterable;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public boolean isAlterable() {
            return alterable;
        }

        public void setAlterable(boolean alterable) {
            this.alterable = alterable;
        }
    }

}
