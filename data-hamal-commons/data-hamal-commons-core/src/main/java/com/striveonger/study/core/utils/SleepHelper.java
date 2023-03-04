package com.striveonger.study.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author Mr.Lee
 * @description:
 * @date 2022-11-30 13:50
 */
public class SleepHelper {
    private final static Logger log = LoggerFactory.getLogger(SleepHelper.class);

    public static void sleepSeconds(int timeout) {
        try {
            TimeUnit.SECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            log.error(Thread.currentThread().getName() + ", 被打断...By SleepHelper", e);
        }
    }

    public static void sleepMilliSeconds(int timeout) {
        try {
            TimeUnit.MILLISECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            log.error(Thread.currentThread().getName() + ", 被打断...By SleepHelper", e);
        }
    }

}