package com.striveonger.study.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-02-22 16:40
 */
@SpringBootApplication
@ComponentScan({"com.striveonger.study.*"})
public class GatewayApplication {
    private final static Logger log = LoggerFactory.getLogger(GatewayApplication.class);

    public static void main(String[] args) {
        // 收集Bean的生命周期相关的信息
        // SpringApplication application = new SpringApplication(GatewayApplication.class);
        // application.setApplicationStartup(new BufferingApplicationStartup(2048));
        // application.run(args);
        SpringApplication.run(GatewayApplication.class, args);
        log.info("(♥◠‿◠)ﾉﾞ  data-hamal-gateway start success  ლ(´ڡ`ლ)ﾞ");
    }
}
