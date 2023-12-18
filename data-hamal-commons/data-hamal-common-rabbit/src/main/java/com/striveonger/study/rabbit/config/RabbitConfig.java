package com.striveonger.study.rabbit.config;

import com.striveonger.study.rabbit.hold.RabbitHold;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-04-10 15:18
 */
@Configuration
public class RabbitConfig {
    private final Logger log = LoggerFactory.getLogger(RabbitConfig.class);

    /* 使用Spring默认的 ConnectionFactory 对象
    @Bean
    public ConnectionFactory connectionFactory() {
        // PooledChannelConnectionFactory
        // ThreadChannelConnectionFactory
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("123456");
        factory.setVirtualHost("/");
        return factory;
    }
    */


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory factory) {
        RabbitTemplate template = new RabbitTemplate(factory);
        // template.setExchange("data-hamal-exchange");
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }

    @Bean
    public RabbitHold rabbitHold(RabbitTemplate template) {
        return new RabbitHold(template);
    }

}
