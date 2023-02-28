package com.striveonger.study.logs;

import ch.qos.logback.classic.LoggerContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-02-27 12:07
 */
public class LogsTest {
    private final Logger log = LoggerFactory.getLogger(LogsTest.class);


    @BeforeEach
    public void before() {
        ILoggerFactory factory = LoggerFactory.getILoggerFactory();
        if (factory instanceof LoggerContext context) {
            ch.qos.logback.classic.Logger root = context.getLogger(Logger.ROOT_LOGGER_NAME);

        }
    }

    @Test
    public void test() {
        log.trace("=====trace=====");
        log.debug("=====debug=====");
        log.info("=====info=====");
        log.warn("=====warn=====");
        log.error("=====error=====");
    }

}
