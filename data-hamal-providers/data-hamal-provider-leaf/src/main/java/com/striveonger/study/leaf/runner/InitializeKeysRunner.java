package com.striveonger.study.leaf.runner;

import com.striveonger.study.redis.holder.RedisHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-04-01 12:28
 */
@Component
public class InitializeKeysRunner implements CommandLineRunner {
    private final Logger log = LoggerFactory.getLogger(InitializeKeysRunner.class);

    @Resource
    private RedisHolder redis;

    @Override
    public void run(String... args) throws Exception {
        RedisHolder.Lock lock = redis.acquireLock();
        boolean acquire = lock.lock("");
        if (acquire) { // 锁定资源

        }
    }
}
