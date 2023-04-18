package com.striveonger.study.redis.holder;

import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import com.striveonger.study.core.utils.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;

import javax.print.DocFlavor;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Set;

import static com.striveonger.study.core.constant.ResultStatus.LOCK_ACQUIRE_FAIL;

/**
 * @author Mr.Lee
 * @description: Redis 操作类
 * @date 2023-03-06 15:00
 */
public class RedisHolder {

    private final Logger log = LoggerFactory.getLogger(RedisHolder.class);

    private final RedisTemplate<String, Object> template;

    private RedisHolder(RedisTemplate<String, Object> template) {
        this.template = template;
    }

    public void putValue(String key, String val) {
        template.opsForValue().set(key, val);
    }

    public <T> void putValue(String key, T obj) {
        String val = JacksonUtils.toJSONString(obj);
        putValue(key, val);
    }

    public String getValue(String key) {
        Object o = template.opsForValue().get(key);
        return o == null ? null : o.toString();
    }

    public <T> T getValue(String key, Class<T> clazz) {
        Object o = template.opsForValue().get(key);
        return o == null ? null : JacksonUtils.toObject(o.toString(), clazz);
    }

    public void removeValue(String key) {
        template.opsForValue().getAndDelete(key);
    }

    public <T> T removeValue(String key, Class<T> clazz) {
        Object o = template.opsForValue().getAndDelete(key);
        return o == null ? null : JacksonUtils.toObject(o.toString(), clazz);
    }

    public <T> boolean setnx(String key, T val) {
        return Optional.ofNullable(template).map(RedisTemplate::opsForValue).map(ops -> ops.setIfAbsent(key, val)).orElse(false);
    }

    /* 放弃原生的bitmap, 使用自定义的数据结构吧
    public void setbit(String key, long offset, boolean value) {
        RedisConnection connection = template.getConnectionFactory().getConnection();
        byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
        connection.setBit(bytes, offset, value);
        connection.close();
    }

    public boolean getbit(String key, long offset) {
        RedisConnection connection = template.getConnectionFactory().getConnection();
        byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
        boolean result = connection.getBit(bytes, offset);
        connection.close();
        return result;
    }
    */

    /**
     * 获取分布式锁
     *
     * @return
     */
    public Lock acquireLock() {
        return new Lock();
    }

    public static class Builder {
        private RedisTemplate<String, Object> template;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder template(RedisTemplate<String, Object> template) {
            this.template = template;
            return this;
        }

        public RedisHolder build() {
            if (template == null) throw new CustomException(ResultStatus.ACCIDENT, "RedisTemplate is NULL");
            return new RedisHolder(this.template);
        }
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
            long lasttime = System.currentTimeMillis() + timeout + 1;
            boolean acquire;
            do {
                acquire = setnx(LOCK_PREFIX + key, expireTime(usetime));
                if (acquire) {
                    return true;
                } else {
                    // 获取超时时间
                    long expiretime = Optional.ofNullable(getValue(LOCK_PREFIX + key)).map(Long::valueOf).orElse(0L);
                    if (expiretime > now()) {
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
            } while (lasttime > now());
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
}
