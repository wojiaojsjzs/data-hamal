package com.striveonger.study.auth;

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

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  认证授权中心启动成功   ლ(´ڡ`ლ)ﾞ");
    }

}