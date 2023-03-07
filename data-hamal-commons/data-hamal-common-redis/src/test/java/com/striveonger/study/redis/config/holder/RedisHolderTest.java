package com.striveonger.study.redis.config.holder;

import com.striveonger.study.core.result.Result;
import com.striveonger.study.redis.config.RedisConfig;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

import static reactor.core.publisher.Mono.*;


/**
 * @author Mr.Lee
 * @description: Redis 操作类
 * @date 2023-03-06 15:00
 */
@ExtendWith(MockitoExtension.class)
public class RedisHolderTest {


    private RedisHolder holder;

    @BeforeEach
    public void setup() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName("localhost");
        config.setPort(6379);
        config.setPassword("123456");
        config.setDatabase(8);
        // GenericObjectPoolConfig<Object> poolConfig = new GenericObjectPoolConfig<>();
        // poolConfig.setMinIdle(0);
        // poolConfig.setMaxIdle(10);
        // poolConfig.setMaxWait(Duration.ofMillis(-1));
        // poolConfig.setMaxTotal(200);
        // LettucePoolingClientConfiguration lettuceConfig = LettucePoolingClientConfiguration.builder().poolConfig(poolConfig).build();
        // LettuceConnectionFactory factory = new LettuceConnectionFactory(config, lettuceConfig);
        LettuceConnectionFactory factory = new LettuceConnectionFactory(config);

        factory.afterPropertiesSet();
        RedisConfig redis = new RedisConfig();
        RedisTemplate<String, Object> template = redis.redisTemplate(factory);
        this.holder = redis.holder(template);
    }

    @Test
    public void test() {
        Result<Object> result = Result.success().data("xxx");
        holder.putValue("data-hamal:KLP:aaa", result);
        result = holder.getValue("data-hamal:KLP:aaa", Result.class);
        System.out.println(result.getData());

        System.out.println("finish~");
    }
}
