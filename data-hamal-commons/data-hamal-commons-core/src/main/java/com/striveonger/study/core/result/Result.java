package com.striveonger.study.core.result;

import com.striveonger.study.core.constant.ResultStatus;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Mr.Lee
 * @description: 公用响应结果
 * @date 2022-11-08 21:36
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 响应状态
     */
    private int state;

    /**
     * 响应时间
     */
    private final LocalDateTime now = LocalDateTime.now();

    /**
     * 响应信息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    private Result() {}

    private Result(ResultStatus status) {
        this.state = status.getState();
        this.message = status.getMessage();
    }

    public Integer getState() {
        return state;
    }

    public LocalDateTime getNow() {
        return now;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public static <T> Result<T> success() {
        return new Result<>(ResultStatus.SUCCESS);
    }

    public static <T> Result<T> fail() {
        return new Result<>(ResultStatus.FAIL);
    }

    public Result<T> message(String message) {
        this.message = message;
        return this;
    }

    public Result<T> data(T data) {
        this.data = data;
        return this;
    }
}