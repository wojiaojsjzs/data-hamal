package com.striveonger.study.redis.holder;

import cn.hutool.core.util.SerializeUtil;
import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.Optional;

import static com.striveonger.study.core.constant.ResultStatus.LOCK_ACQUIRE_FAIL;

/**
 * @author Mr.Lee
 * @description: Redis 操作类
 * @date 2023-03-06 15:00
 */
public class RedisHolder {

    private final Logger log = LoggerFactory.getLogger(RedisHolder.class);

    private final RedisTemplate<String, byte[]> template;

    private RedisHolder(RedisTemplate<String, byte[]> template) {
        this.template = template;
    }

    public <T extends Serializable> void putValue(String key, T val) {
        byte[] bytes = SerializeUtil.serialize(val);
        setBytes(key, bytes);
    }

    public <T extends Serializable> T getValue(String key) {
        byte[] bytes = getBytes(key);
        return bytes == null ? null : SerializeUtil.deserialize(bytes);
    }

    public void removeValue(String key) {
        template.opsForValue().getAndDelete(key);
    }

    public <T> T getValueAndRemove(String key) {
        byte[] bytes = template.opsForValue().getAndDelete(key);
        return bytes == null ? null : SerializeUtil.deserialize(bytes);
    }

    public <T> boolean setnx(String key, T val) {
        byte[] bytes = SerializeUtil.serialize(val);
        return Optional.ofNullable(template).map(RedisTemplate::opsForValue).map(ops -> ops.setIfAbsent(key, bytes)).orElse(false);
    }

    public void setBytes(String key, byte[] bytes) {
        template.opsForValue().set(key, bytes);
    }

    public byte[] getBytes(String key) {
        return template.opsForValue().get(key);
    }

    /**
     * 获取分布式锁
     *
     * @return
     */
    public Lock acquireLock() {
        return new Lock();
    }

    /**
     * Redis实现的分布式锁
     */
    public class Lock {
        private static final String LOCK_PREFIX = "data-hamal:lock:";

        private static final int DEFAULT_USETIME = 30 * 1000; // default 30s;

        /**
         * 获得锁
         * 用此方法, 锁定资源时, 用完一定要记得释放锁(未设置超时机制)
         *
         * @param key 锁标识
         * @return
         */
        public boolean lock(String key) {
            return lock(key, DEFAULT_USETIME);
        }

        /**
         * 获得锁
         * 用此方法, 锁定资源时, 用完一定要记得释放锁(未设置超时机制)
         *
         * @param key     锁标识
         * @param usetime 最长使用时间(毫秒)
         * @return
         */
        public boolean lock(String key, long usetime) {
            return setnx(LOCK_PREFIX + key, expireTime(usetime));
        }

        /**
         * 尝试获得锁
         *
         * @param key     锁标识
         * @param timeout 最长等待时间(毫秒)
         * @return
         */
        public boolean tryLock(String key, long timeout) {
            return tryLock(key, DEFAULT_USETIME, timeout);
        }

        /**
         * 尝试获得锁
         *
         * @param key     锁标识
         * @param usetime 最大使用时长(毫秒)
         * @param timeout 最大等待时长(毫秒)
         * @return
         */
        public boolean tryLock(String key, long usetime, long timeout) {
            long lastTime = System.currentTimeMillis() + timeout + 1;
            boolean acquire;
            do {
                acquire = setnx(LOCK_PREFIX + key, expireTime(usetime));
                if (acquire) {
                    return true;
                } else {
                    // 获取超时时间
                    Long expireTime = getValue(LOCK_PREFIX + key);
                    // expireTime = expireTime == null ? 0L : expireTime;
                    if (expireTime > now()) {
                        // sleep
                        try {
                            log.info("trylock thread sleep...");
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            throw new CustomException(LOCK_ACQUIRE_FAIL, "The thread was interrupted.");
                        }
                    } else {
                        // 删除失效的锁
                        removeValue(LOCK_PREFIX + key);
                        System.out.println("tryLock remove key: " + key);
                    }
                }
            } while (lastTime > now());
            return false;
        }

        public void unlock(String key) {
            removeValue(LOCK_PREFIX + key);
        }

        private long expireTime(long expire) {
            return now() + expire + 1;
        }

        private long now() {
            return System.currentTimeMillis();
        }
    }

    public static class Builder {
        private RedisTemplate<String, byte[]> template;

        private Builder() { }

        public static Builder builder() {
            return new Builder();
        }

        public Builder template(RedisTemplate<String, byte[]> template) {
            this.template = template;
            return this;
        }


        public RedisHolder build() {
            if (template == null) throw new CustomException(ResultStatus.ACCIDENT, "RedisTemplate is NULL");
            return new RedisHolder(template);
        }
    }
}
