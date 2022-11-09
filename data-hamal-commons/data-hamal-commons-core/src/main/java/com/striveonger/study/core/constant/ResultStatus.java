package com.striveonger.study.core.constant;

/**
 * @author Mr.Lee
 * @description: 通用状态码
 * @date 2022-11-08 22:05
 */
public enum ResultStatus {
    SUCCESS(200, "Success"),
    FAIL(0, "Fail"),

    NOT_FOUND(404, "Not Found");


    private final int state;
    private final String message;

    ResultStatus(int state, String message) {
        this.state = state;
        this.message = message;
    }

    public int getState() {
        return state;
    }

    public String getMessage() {
        return message;
    }
}
