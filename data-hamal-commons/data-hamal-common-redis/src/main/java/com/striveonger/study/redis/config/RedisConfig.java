package com.striveonger.study.redis.config;

import com.striveonger.study.redis.holder.RedisHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-03-06 14:11
 */
@Configuration
// @ConditionalOnClass({ GenericObjectPool.class, JedisConnection.class, RedisProperties.Jedis.class })
// @ConditionalOnMissingBean(RedisConnectionFactory.class)
// @ConditionalOnProperty(name = "spring.data.redis.client-type", havingValue = "jedis", matchIfMissing = true)
public class RedisConfig {
    private final Logger log = LoggerFactory.getLogger(RedisConfig.class);

    public RedisTemplate<String, byte[]> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        // template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        // // value以二进制的形式进行序列化
        template.setValueSerializer(new JdkSerializationRedisSerializer());
        template.setHashValueSerializer(new JdkSerializationRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisHolder holder(RedisTemplate<String, byte[]> template) {
        return RedisHolder.Builder.builder().template(template).build();
    }
}
