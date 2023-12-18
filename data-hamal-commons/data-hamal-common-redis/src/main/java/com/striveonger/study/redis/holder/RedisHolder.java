package com.striveonger.study.redis.holder;

import cn.hutool.core.util.SerializeUtil;
import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import org.redisson.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author Mr.Lee
 * @description: Redis 操作类
 * @date 2023-03-06 15:00
 */
public class RedisHolder {

    private final Logger log = LoggerFactory.getLogger(RedisHolder.class);

    private final RedissonClient client;
    private final Lock localock;

    private RedisHolder(RedissonClient client) {
        this.client = client;
        this.localock = new Lock();
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
        RBucket<Object> bucket = client.getBucket(key);
        bucket.delete();
    }

    public <T extends Serializable> T getValueAndRemove(String key) {
        RBucket<byte[]> bucket = client.getBucket(key);
        byte[] bytes = bucket.getAndDelete();
        return bytes == null ? null : SerializeUtil.deserialize(bytes);
    }

    public <T> boolean setnx(String key, T val) {
        byte[] bytes = SerializeUtil.serialize(val);
        RBucket<byte[]> bucket = client.getBucket(key);
        return bucket.setIfAbsent(bytes); // 用的就是 SETNX 命令
        // Script 方式, 返回值一直是NULL, 不知道为啥...
        // client.getScript().eval(RScript.Mode.READ_WRITE, "redis.call('SET', KEYS[1], ARGV[1], 'NX')", RScript.ReturnType.STATUS, List.of(key), bytes);
    }

    public void setBytes(String key, byte[] bytes) {
        RBucket<byte[]> bucket = client.getBucket(key);
        bucket.set(bytes);
    }

    public byte[] getBytes(String key) {
        RBucket<byte[]> bucket = client.getBucket(key);
        return bucket.get();
    }

    /**
     * 获取分布式锁
     *
     * @return
     */
    public Lock acquireLock() {
        return localock;
    }

    // /**
    //  * Redis实现的分布式锁(SETNX实现)
    //  */
    // @Deprecated
    // public class Lock {
    //     private static final String LOCK_PREFIX = "data-hamal:lock:";
    //
    //     private static final int DEFAULT_USETIME = 30 * 1000; // default 30s;
    //
    //     /**
    //      * 获得锁
    //      * 用此方法, 锁定资源时, 用完一定要记得释放锁(未设置超时机制)
    //      *
    //      * @param key 锁标识
    //      * @return
    //      */
    //     public boolean lock(String key) {
    //         return lock(key, DEFAULT_USETIME);
    //     }
    //
    //     /**
    //      * 获得锁
    //      * 用此方法, 锁定资源时, 用完一定要记得释放锁(未设置超时机制)
    //      *
    //      * @param key     锁标识
    //      * @param usetime 最长使用时间(毫秒)
    //      * @return
    //      */
    //     public boolean lock(String key, long usetime) {
    //         return setnx(LOCK_PREFIX + key, expireTime(usetime));
    //     }
    //
    //     /**
    //      * 尝试获得锁
    //      *
    //      * @param key     锁标识
    //      * @param timeout 最长等待时间(毫秒)
    //      * @return
    //      */
    //     public boolean tryLock(String key, long timeout) {
    //         return tryLock(key, DEFAULT_USETIME, timeout);
    //     }
    //
    //     /**
    //      * 尝试获得锁
    //      *
    //      * @param key     锁标识
    //      * @param usetime 最大使用时长(毫秒)
    //      * @param timeout 最大等待时长(毫秒)
    //      * @return
    //      */
    //     public boolean tryLock(String key, long usetime, long timeout) {
    //         long lastTime = System.currentTimeMillis() + timeout + 1;
    //         boolean acquire;
    //         do {
    //             acquire = setnx(LOCK_PREFIX + key, expireTime(usetime));
    //             if (acquire) {
    //                 return true;
    //             } else {
    //                 // 获取超时时间
    //                 Long expireTime = getValue(LOCK_PREFIX + key);
    //                 // expireTime = expireTime == null ? 0L : expireTime;
    //                 if (expireTime > now()) {
    //                     // sleep
    //                     try {
    //                         log.info("trylock thread sleep...");
    //                         Thread.sleep(50);
    //                     } catch (InterruptedException e) {
    //                         throw new CustomException(LOCK_ACQUIRE_FAIL, "The thread was interrupted.");
    //                     }
    //                 } else {
    //                     // 删除失效的锁
    //                     removeValue(LOCK_PREFIX + key);
    //                     System.out.println("tryLock remove key: " + key);
    //                 }
    //             }
    //         } while (lastTime > now());
    //         return false;
    //     }
    //
    //     public void unlock(String key) {
    //         removeValue(LOCK_PREFIX + key);
    //     }
    //
    //     private long expireTime(long expire) {
    //         return now() + expire + 1;
    //     }
    //
    //     private long now() {
    //         return System.currentTimeMillis();
    //     }
    // }


    /**
     * 分布式锁(Redisson实现)
     */
    public class Lock {

        /**
         * 使用 Redis 的 SETNX 命令尝试获取锁。SETNX 命令会在 Redis 中设置一个键值对，仅当键不存在时才会成功，并返回 1，表示成功获取到锁。
         * 如果获取锁成功，则设置锁的过期时间，以防止锁无法释放。这可以通过 Redis 的 PEXPIRE 命令来实现。
         * 如果获取锁失败，则使用 Redis 的 BLPOP 命令在一个特定的队列上等待锁的释放。BLPOP 是一个阻塞式命令，它会一直阻塞直到队列中有元素可弹出。
         * 当锁的持有者释放锁时，Redisson 会使用 Redis 的 DEL 命令来删除锁。
         * Redisson 还提供了以下额外的特性来增强分布式锁的功能和可靠性：
         * 可重入锁：Redisson 支持可重入锁，允许同一个线程多次获取同一个锁，避免死锁问题。
         * 公平锁：Redisson 提供了公平锁的实现，保证锁的获取按照申请的顺序进行，避免线程饥饿问题。
         * 读写锁：Redisson 还支持读写锁，允许多个线程同时读取共享资源，但只允许一个线程进行写操作。
         * 锁的续期：Redisson 允许在持有锁的期间自动续期锁的过期时间，确保业务逻辑执行时间超过锁的过期时间时不会释放锁。
         * 监听锁的释放事件：Redisson 支持监听锁的释放事件，以便在锁被释放时执行一些特定的逻辑。
         * 使用 Redisson 实现分布式锁非常简单，只需要引入 Redisson 的依赖，并使用 Redisson 的 RLock 接口来进行锁的获取和释放。
         */

        private static final String LOCK_PREFIX = "data-hamal:lock:";
        private static final int DEFAULT_USETIME = 30 * 1000; // default 30s;

        /**
         * 尝试获得锁, 如果没有获得锁,会阻塞等待获得锁
         *
         * @param key 锁标识
         * @return
         */
        public void lock(String key) {
            lock(key, DEFAULT_USETIME);
        }

        /**
         * 尝试获得锁, 如果没有获得锁,会阻塞等待获得锁
         *
         * @param key     锁标识
         * @param usetime 最长使用时间(毫秒)
         * @return
         */
        public void lock(String key, long usetime) {
            key = LOCK_PREFIX + key;
            RLock lock = client.getLock(key);
            lock.lock(usetime, TimeUnit.MILLISECONDS);
        }

        /**
         * 尝试获得锁
         *
         * @param key      锁标识
         * @param waitTime 最长等待时间(毫秒)
         * @return
         */
        public boolean tryLock(String key, long waitTime) {
            return tryLock(key, DEFAULT_USETIME, waitTime);
        }

        /**
         * 尝试获得锁
         *
         * @param key      锁标识
         * @param usetime  最大使用时长(毫秒)
         * @param waitTime 最大等待时长(毫秒)
         * @return
         */
        public boolean tryLock(String key, long usetime, long waitTime) {
            key = LOCK_PREFIX + key;
            RLock lock = client.getLock(key);
            try {
                return lock.tryLock(waitTime, usetime, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                log.error("An attempt to acquire a lock failed... ", e);
            }
            return false;
        }


        public void unlock(String key) {
            key = LOCK_PREFIX + key;
            RLock lock = client.getLock(key);
            lock.unlock();
        }


    }

    public static class Builder {

        private RedissonClient client;

        private Builder() { }

        public static Builder builder() {
            return new Builder();
        }

        public Builder client(RedissonClient client) {
            this.client = client;
            return this;
        }

        public RedisHolder build() {
            if (client == null) {
                throw new CustomException(ResultStatus.ACCIDENT, "RedissonClient is NULL");
            }
            return new RedisHolder(client);
        }
    }
}
