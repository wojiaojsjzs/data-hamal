package com.striveonger.study.redis.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-03-30 11:59
 */
public class Lock {
    private final Logger log = LoggerFactory.getLogger(Lock.class);

    public boolean lock() {
        return false;
    }

    public boolean tryLock() {
        return false;
    }

    public boolean tryLock(long time, TimeUnit unit) {
        return false;
    }

    public void unlock() {

    }
}
