package com.striveonger.study.task.common;

/**
 * @author Mr.Lee
 * @description: 监听器
 * @date 2023-07-12 6:11
 */
public interface Listener {

    /**
     * 获取优先级
     */
    public default Priority getPriority() {
        return Priority.NORM;
    }

    /**
     * 获取执行顺序
     * 该方法, 不建议重写覆盖
     */
    public default int order() {
        return getPriority().getVal();
    }

    /**
     * 表示监听器的执行优先级
     * 优先级越高, 执行顺序越靠前
     */
    public static enum Priority {

        MIN(1), NORM(5), MAX(10);

        private final int val;

        Priority(int val) {
            this.val = val;
        }

        public int getVal() {
            return val;
        }
    }
}
