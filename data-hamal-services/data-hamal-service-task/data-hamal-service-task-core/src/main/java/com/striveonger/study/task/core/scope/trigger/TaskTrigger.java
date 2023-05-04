package com.striveonger.study.task.core.scope.trigger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mr.Lee
 * @description: 任务触发器
 * @date 2023-05-04 15:35
 */
public class TaskTrigger {
    private long taskID;
    private List<PerformParam> params;

    public long getTaskID() {
        return taskID;
    }

    public void setTaskID(long taskID) {
        this.taskID = taskID;
    }

    public List<PerformParam> getParams() {
        return params;
    }

    public void setParams(List<PerformParam> params) {
        this.params = params;
    }

    public void setParam(PerformParam param) {
        if (params == null) {
            params = new ArrayList<>();
        }
        params.add(param);
    }


    /**
     * 执行参数
     */
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
