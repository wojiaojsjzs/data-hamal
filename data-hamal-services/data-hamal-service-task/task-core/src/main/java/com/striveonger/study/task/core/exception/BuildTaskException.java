package com.striveonger.study.task.core.exception;

/**
 * @author Mr.Lee
 * @description: 构建Task发生异常
 * @date 2022-12-03 12:32
 */
public class BuildTaskException extends RuntimeException {

    /**
     * 异常类型
     */
    private final Type type;

    // 工作台
    public BuildTaskException() {
        // 默认的异常类型
        this(Type.TASK);
    }

    public BuildTaskException(Type type) {
        super(type.message);
        this.type = type;
    }
    public BuildTaskException(Type type, String message) {
        super(message);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        /**
         * Task构建失败
         */
        TASK("Task build failure..."),

        /**
         * Step构建失败
         */
        STEP("Step build failure..."),

        /**
         * 工作台构建失败
         */
        WORKBENCH("Workbench build failure...");


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