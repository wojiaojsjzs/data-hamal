package com.striveonger.study.task.common.scope.context;

import com.striveonger.study.task.common.constant.StepStatus;
import com.striveonger.study.task.common.constant.TaskStatus;
import com.striveonger.study.task.common.scope.status.RuntimeStatus;
import com.striveonger.study.task.common.scope.context.storage.ContextStorage;

import java.util.Objects;

/**
 * @author Mr.Lee
 * @description: 运行时上下文对象
 * @date 2023-05-04 18:18
 */
public class RuntimeContext {

    private final ContextStorage storage;

    private final StatusHolder status;

    public RuntimeContext(ContextStorage storage) {
        this.storage = storage;
        this.status = new StatusHolder();
    }

    public StatusHolder status() {
        return this.status;
    }

    /**
     * 运行时状态的操作对象
     */
    public class StatusHolder {
        private StatusHolder() { }

        public void start(String taskID, int total) {
            synchronized (taskID.intern()) {
                if (!storage.containsKey(taskID)) {
                    RuntimeStatus status = new RuntimeStatus(total);
                    storage.put(taskID, status);
                }
            }
        }

        public void stop(String taskID) {
            storage.remove(taskID);
        }

        public void update(String taskID, int index, StepStatus status) {
            synchronized (taskID.intern()) {
                RuntimeStatus holder = storage.get(taskID);
                if (Objects.nonNull(holder)) {
                    holder.update(index, status);
                }
            }
        }

        public StepStatus stepStatus(String taskID, int index) {
            RuntimeStatus holder = storage.get(taskID);
            if (Objects.nonNull(holder)) {
                return holder.stepStatus(index);
            }
            return null;
        }

        public TaskStatus taskStatus(String taskID) {
            RuntimeStatus holder = storage.get(taskID);
            if (Objects.nonNull(holder)) {
                return holder.taskStatus();
            }
            return null;
        }
    }


}
