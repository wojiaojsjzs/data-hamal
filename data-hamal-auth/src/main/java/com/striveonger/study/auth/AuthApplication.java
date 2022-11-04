package com.striveonger.study.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * @author Mr.Lee
 * @description:
 * @date 2022-11-03 23:20
 */
@SpringBootApplication
public class AuthApplication {
    private static final Logger log = LoggerFactory.getLogger(AuthApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
        log.info("(♥◠‿◠)ﾉﾞ  data-hamal-auth start success  ლ(´ڡ`ლ)ﾞ");
    }
}