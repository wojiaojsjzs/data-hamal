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
public class HelloProviderApplication {

    private static final Logger log = LoggerFactory.getLogger(HelloProviderApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(HelloProviderApplication.class, args);
        log.info("(♥◠‿◠)ﾉﾞ  data-hamal-provider-hello start success  ლ(´ڡ`ლ)ﾞ");

    }

}
