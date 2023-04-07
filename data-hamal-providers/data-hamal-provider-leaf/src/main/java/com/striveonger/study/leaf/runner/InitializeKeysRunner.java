package com.striveonger.study.leaf.runner;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.striveonger.study.api.leaf.constant.Keys;
import com.striveonger.study.core.constant.DistractLockKeys;
import com.striveonger.study.leaf.entity.LeafAlloc;
import com.striveonger.study.leaf.service.ILeafAllocService;
import com.striveonger.study.redis.holder.RedisHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.striveonger.study.core.constant.DistractLockKeys.INITIALIZE_LEAF_KEY;

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

    @Resource
    private ILeafAllocService db;

    @Override
    public void run(String... args) throws Exception {
        RedisHolder.Lock lock = redis.acquireLock();
        try {
            boolean acquire = lock.lock(INITIALIZE_LEAF_KEY.key());
            if (acquire) { // 锁定资源
                // 同一时间, 只能有一个线程进行初始化操作
                for (Keys item : Keys.values()) {
                    int x = db.count(item.getKey());
                    if (x == 0) {
                        LeafAlloc alloc = new LeafAlloc();
                        alloc.setKey(item.getKey());
                        alloc.setStep(item.getStep());
                        alloc.setMaxId(item.getStart());
                        alloc.setDescription(item.getDescription());
                        alloc.setUpdateTime(DateUtil.now());
                        db.save(alloc);
                    }
                }
            }
        } finally {
            lock.unlock(INITIALIZE_LEAF_KEY.key());
        }
    }
}
