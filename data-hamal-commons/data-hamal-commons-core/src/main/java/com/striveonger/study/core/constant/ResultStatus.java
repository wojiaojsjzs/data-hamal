package com.striveonger.study.core.constant;

/**
 * @author Mr.Lee
 * @description: 通用状态码
 * @date 2022-11-08 22:05
 */
public enum ResultStatus {

    /**
     * 请求成功标识
     */
    SUCCESS(200, "Success"),

    /**
     * 默认请求失败标识
     * 没有准确定义的错误信息, 可以使用
     */
    FAIL(0, "Fail"),

    /**
     * 资源找不到的错误标识
     */
    NOT_FOUND(404, "Not Found"),

    /**
     * 系统发生异常的标识
     */
    ACCIDENT(500, "Accident Error");

    private final int state;
    private final String message;

    ResultStatus(int state, String message) {
        this.state = state;
        this.message = message;
    }

    public int getState() {
        return state;
    }

    public String getCode() {
        return this.toString();
    }

    public String getMessage() {
        return message;
    }
}
