package com.striveonger.study.core.handler;

import com.striveonger.study.core.exception.CustomException;
import com.striveonger.study.core.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mr.Lee
 * @description:
 * @date 2022-11-12 12:38
 */
@ControllerAdvice
public class WebExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(WebExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler(CustomException.class)
    public Result customExceptionHandler(CustomException e, HttpServletRequest request) {
        log.error(e.getMessage(), e);
        return Result.status(e.getStatus()).message(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Result exceptionHandler(Exception e, HttpServletRequest request) {
        log.error(e.getMessage(), e);
        return Result.accident().message(e.getMessage());
    }
}