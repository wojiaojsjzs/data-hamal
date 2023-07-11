package com.striveonger.study.redis.config;

import com.striveonger.study.core.exception.CustomException;
import com.striveonger.study.redis.holder.RedisHolder;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.striveonger.study.core.constant.ResultStatus.NON_SUPPORT;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-03-06 14:11
 */
@Configuration
public class RedisConfig {
    private final Logger log = LoggerFactory.getLogger(RedisConfig.class);

    // public RedisTemplate<String, byte[]> redisTemplate(RedisConfigProperties factory) {
    //     RedisTemplate<String, byte[]> template = new RedisTemplate<>();
    //     template.setConnectionFactory(factory);
    //     template.setKeySerializer(new StringRedisSerializer());
    //     template.setHashKeySerializer(new StringRedisSerializer());
    //     // template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    //     // template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
    //     // // value以二进制的形式进行序列化
    //     template.setValueSerializer(new JdkSerializationRedisSerializer());
    //     template.setHashValueSerializer(new JdkSerializationRedisSerializer());
    //     template.afterPropertiesSet();
    //     return template;
    // }

    // @Bean
    // public RedisHolder holder(RedisTemplate<String, byte[]> template) {
    //     return RedisHolder.Builder.builder().template(template).build();
    // }


    /*
    创建 RedissonClient 的方式有以下几种：

    1. 使用单节点模式创建 RedissonClient：
       ```java
       Config config = new Config();
       config.useSingleServer().setAddress("redis://localhost:6379");
       RedissonClient redisson = Redisson.create(config);
       ```

    2. 使用主从模式创建 RedissonClient：
       ```java
       Config config = new Config();
       config.useMasterSlaveServers()
             .setMasterAddress("redis://localhost:6379")
             .addSlaveAddress("redis://localhost:6380")
             .addSlaveAddress("redis://localhost:6381");
       RedissonClient redisson = Redisson.create(config);
       ```

    3. 使用哨兵模式创建 RedissonClient：
       ```java
       Config config = new Config();
       config.useSentinelServers()
             .addSentinelAddress("redis://localhost:26379")
             .addSentinelAddress("redis://localhost:26380")
             .addSentinelAddress("redis://localhost:26381")
             .setMasterName("mymaster");
       RedissonClient redisson = Redisson.create(config);
       ```

    4. 使用集群模式创建 RedissonClient：
       ```java
       Config config = new Config();
       config.useClusterServers()
             .addNodeAddress("redis://localhost:7000")
             .addNodeAddress("redis://localhost:7001")
             .addNodeAddress("redis://localhost:7002")
             .addNodeAddress("redis://localhost:7003")
             .addNodeAddress("redis://localhost:7004")
             .addNodeAddress("redis://localhost:7005");
       RedissonClient redisson = Redisson.create(config);
       ```

    5. 使用自定义配置创建 RedissonClient：
       ```java
       Config config = new Config();
       // 自定义配置，例如设置连接池大小、连接超时时间等
       config.useSingleServer()
             .setAddress("redis://localhost:6379")
             .setConnectionPoolSize(10)
             .setConnectTimeout(5000);
       RedissonClient redisson = Redisson.create(config);
       ```

    以上是创建 RedissonClient 的几种常见方式，你可以根据具体的部署环境和需求选择适合的方式进行创建。无论使用哪种方式，都需要先创建一个 Config 对象，然后根据具体的模式或配置来设置 Redis 的连接信息，最后通过 Redisson.create(config) 方法来创建 RedissonClient 实例。
     */

    @Bean
    public RedissonClient redissonClient(RedisConfigProperties properties) {
        Config config = new Config();

        String model = properties.getModel();

        if ("single".equals(model)) {
            RedisConfigProperties.Single single = properties.getSingle();
            // 单节点模式
            config.useSingleServer()
                  .setAddress(single.getAddress())
                  .setDatabase(single.getDatabase())
                  .setUsername(properties.getUsername())
                  .setPassword(properties.getPassword());
            return Redisson.create(config);
        }

        throw new CustomException(NON_SUPPORT, "其他模式, 暂时不支持");
    }

    @Bean
    public RedisHolder holder(RedissonClient client) {
        return RedisHolder.Builder.builder().client(client).build();
    }



}
