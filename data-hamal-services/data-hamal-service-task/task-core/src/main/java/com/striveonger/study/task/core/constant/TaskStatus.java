package com.striveonger.study.task.core.constant;

/**
 * @author Mr.Lee
 * @description: 任务执行状态
 * @date 2022-11-30 09:29
 */
public enum TaskStatus {
    /**
     * 尚未开始
     */
    NONE(0),
    /**
     * 执行中
     */
    RUNNING(1),
    /**
     * 执行完成
     */
    COMPLETE(3),
    /**
     * 执行失败
     */
    FAIL(2),
    /**
     * 暂停状态(只针对于任务，步骤在执行中，不会被暂停)
     */
    SUSPEND(4),
    /**
     * 中止状态(只针对于任务，步骤在执行中，不会被中止)
     */
    ABORT(5);

    private final int code;

    TaskStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static TaskStatus of(int code) {
        for (TaskStatus value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        return null;
    }
}