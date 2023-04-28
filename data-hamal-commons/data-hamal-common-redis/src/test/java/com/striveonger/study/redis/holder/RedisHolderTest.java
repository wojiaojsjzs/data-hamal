package com.striveonger.study.redis.holder;

import cn.hutool.core.thread.ThreadUtil;
import com.striveonger.study.core.utils.SleepHelper;
import com.striveonger.study.redis.config.RedisConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author Mr.Lee
 * @description: Redis 操作类
 * @date 2023-03-06 15:00
 */
// @ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {"logging.level.root=ERROR"})
public class RedisHolderTest {
    private final Logger log = LoggerFactory.getLogger(RedisHolderTest.class);

    private RedisHolder holder;

    @BeforeEach
    public void setup() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName("localhost");
        config.setPort(6379);
        config.setPassword("123456");
        config.setDatabase(1);
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
        RedisTemplate<String, byte[]> template = redis.redisTemplate(factory);
        this.holder = redis.holder(template);
    }

    @Test
    public void test() {
        // Result result = Result.success().data("xxx");
        // holder.putValue("data-hamal:KLP:aaa", JacksonUtils.toJSONString(result));
        // String s = holder.getValue("data-hamal:KLP:aaa");
        // System.out.println(s);
        // result = JacksonUtils.toObject(s, Result.class);
        // assert result != null;
        // System.out.println(result.getData());


        System.out.println("------------------------");
        // byte[] bytes = SerializeUtil.serialize(result);
        // String key = "data-hamal:KLP:bbb";
        // holder.setBytes(key, bytes);
        // bytes = holder.getBytes(key);
        // result = SerializeUtil.deserialize(bytes);
        // System.out.println(JacksonUtils.toJSONString(result));

        // long expireTime = System.currentTimeMillis() + 12 * 60 * 1000 + 1;
        // String key = "data-hamal:lock:test";
        // boolean acquire = holder.setnx(key, expireTime);
        // System.out.println("acquire: " + acquire);
        // System.out.println("value: " + holder.getValue(key));
        //
        // holder.removeValue(key);
        // System.out.println(holder.getValue(key, Long.class));


        // for (int i = 0; i < 1000000000; i++) {
        //     boolean X1 = holder.setnx("TYO", "x"), X2 = holder.setnx("TYO", "x");
        //     if (!X1 || X2) {
        //         System.out.println("X1: " + X1 + " , X2: " + X2);
        //         System.out.println("Oops...");
        //         break;
        //     }
        //     holder.removeValue("TYO");
        // }

        System.out.println("-------------------------------------------------------------");
        ExecutorService pool = Executors.newCachedThreadPool();
        Thread t1 = new Thread(() -> {
            log.error("T1 start...");
            RedisHolder.Lock lock = holder.acquireLock();
            boolean acquire = lock.lock("test");
            log.error("T1 acquire: " + acquire);
            if (acquire) {
                // 持有锁的时间
                ThreadUtil.sleep(5 * 1000); // 占用时间 5s
                log.error("T1 unlock");
                lock.unlock("test");
            }
            log.error("T1 end...");
        }, "T1");

        Thread t2 = new Thread(() -> {
            log.error("T2 start...");
            Thread.yield();
            // SleepHelper.sleepMilliSeconds(50);
            // log.error("T2 start...");
            RedisHolder.Lock lock = holder.acquireLock();
            boolean acquire = lock.tryLock("test", 1000 * 3); // 等待时间 3s

            log.error("T2 acquire: " + acquire);
            if (acquire) {
                // 持有锁的时间
                ThreadUtil.sleep(5 * 1000);
                log.error("T2 unlock");
                lock.unlock("test");
            }
            log.error("T2 end...");
        }, "T2");

        pool.submit(t1); pool.submit(t2);

        ThreadUtil.sleep(3 * 60 * 1000);
        System.out.println("finish~");
    }
}
