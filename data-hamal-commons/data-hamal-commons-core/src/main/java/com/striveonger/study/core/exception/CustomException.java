package com.striveonger.study.core.exception;

import com.striveonger.study.core.constant.ResultStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mr.Lee
 * @description:
 * @date 2022-11-12 13:12
 */
public class CustomException extends RuntimeException {
    private final Logger log = LoggerFactory.getLogger(CustomException.class);

    private final ResultStatus status;

    private final String message;

    public CustomException(ResultStatus status) {
        super(status.getMessage());
        this.status = status;
        this.message = status.getMessage();
    }

    public CustomException(String message) {
        super(message);
        this.status = ResultStatus.ACCIDENT;
        this.message = message;
    }

    public CustomException(ResultStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public ResultStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}