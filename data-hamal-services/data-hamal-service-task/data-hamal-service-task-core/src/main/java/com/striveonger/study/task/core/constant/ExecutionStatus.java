package com.striveonger.study.task.core.constant;

/**
 * @author Mr.Lee
 * @description: 执行状态
 * @date 2022-11-30 09:29
 */
public enum ExecutionStatus {
    /**
     * 尚未开始
     */
    NONE(0),
    /**
     * 执行中
     */
    RUNNING(1),
    /**
     * 暂停状态（只针对于任务，步骤在执行中，不会暂停的）
     */
    SUSPEND(2),
    /**
     * 执行完成
     */
    COMPLETE(3),
    /**
     * 执行失败
     */
    FAIL(4);

    private final int code;

    ExecutionStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}