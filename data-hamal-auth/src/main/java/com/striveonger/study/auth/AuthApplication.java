package com.striveonger.study.auth;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Mr.Lee
 * @description:
 * @date 2022-11-03 23:20
 */
@EnableDubbo
@SpringBootApplication
@ComponentScan({"com.striveonger.study.*"})
public class AuthApplication {
    private static final Logger log = LoggerFactory.getLogger(AuthApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
        log.info("(♥◠‿◠)ﾉﾞ  data-hamal-user start success  ლ(´ڡ`ლ)ﾞ");
    }
}