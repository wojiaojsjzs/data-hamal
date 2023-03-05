package com.striveonger.study.core.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.striveonger.study.core.constant.ResultStatus;

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
     * 响应码
     */
    private String code;

    /**
     * 响应时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    private final LocalDateTime now = LocalDateTime.now();

    /**
     * 响应信息
     */
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    private Result() { }

    private Result(ResultStatus status) {
        this.state = status.getState();
        this.code = status.getCode();
        this.message = status.getMessage();
    }

    public Integer getState() {
        return state;
    }

    public String getCode() {
        return code;
    }

    public LocalDateTime getNow() {
        return now;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return this.data;
    }

    public static <T> Result<T> success() {
        return new Result<>(ResultStatus.SUCCESS);
    }

    public static <T> Result<T> success(T data) {
        return new Result<T>(ResultStatus.SUCCESS).data(data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<T>(ResultStatus.SUCCESS).message(message).data(data);
    }

    public static <T> Result<T> fail() {
        return new Result<>(ResultStatus.FAIL);
    }

    public static <T> Result<T> accident() {
        return new Result<T>(ResultStatus.ACCIDENT);
    }

    public static <T> Result<T> status(ResultStatus status) {
        return new Result<>(status);
    }

    public static <T> Result<T> status(boolean status) {
        return new Result<>(status ? ResultStatus.SUCCESS : ResultStatus.FAIL);
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