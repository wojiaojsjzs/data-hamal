package com.striveonger.study.hello;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-03-09 13:49
 */
@EnableDubbo
@SpringBootApplication
public class HelloServiceApplication {

    private static final Logger log = LoggerFactory.getLogger(HelloServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(HelloServiceApplication.class, args);
        log.info("(♥◠‿◠)ﾉﾞ  data-hamal-service-hello start success  ლ(´ڡ`ლ)ﾞ");
    }

}
