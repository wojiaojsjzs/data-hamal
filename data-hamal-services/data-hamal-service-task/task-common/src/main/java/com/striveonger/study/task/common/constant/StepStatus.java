package com.striveonger.study.task.common.constant;

/**
 * @author Mr.Lee
 * @description: 步骤执行状态(用两位表示一个任务的执行状态)
 * @date 2023-04-17 14:37
 */
public enum StepStatus {

    /**
     * 尚未开始(00)
     */
    NONE(0),
    /**
     * 执行中(01)
     */
    RUNNING(1),

    /**
     * 执行完成(11)
     */
    COMPLETE(3),
    /**
     * 执行失败(10)
     */
    FAIL(2);

    private final int code;

    StepStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static StepStatus of(int code) {
        for (StepStatus value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        return null;
    }
}
