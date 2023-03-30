package com.striveonger.study.redis.holder;

import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import com.striveonger.study.core.utils.JacksonUtils;
import com.striveonger.study.redis.lock.Lock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

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

    public Lock acquireLock() {
        return new Lock();
    }

    public static class Builder {
        private RedisTemplate<String, Object> template;

        private Builder() {}

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
}
