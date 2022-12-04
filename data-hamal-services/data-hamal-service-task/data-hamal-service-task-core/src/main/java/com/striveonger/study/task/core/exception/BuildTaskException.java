package com.striveonger.study.task.core.exception;

/**
 * @author Mr.Lee
 * @description:
 * @date 2022-12-03 12:32
 */
public class BuildTaskException extends RuntimeException {

    public BuildTaskException() {
        this(Type.TASK_AREA);
    }

    public BuildTaskException(Type type) {
        super(type.message);
    }

    public enum Type {
        TASK_AREA("Task Build Fail..."),

        WORK_AREA("WorkArea Build Fail...");
        private final String message;

        Type(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return message;
        }
    }

}