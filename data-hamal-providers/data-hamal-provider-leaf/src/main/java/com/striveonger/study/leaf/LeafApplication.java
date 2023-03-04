package com.striveonger.study.leaf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Mr.Lee
 * @description: 秋天的叶子
 * @date 2022-11-18 23:18
 */
@SpringBootApplication
@ComponentScan({"com.striveonger.study.*"})
public class LeafApplication {

    private static final Logger log = LoggerFactory.getLogger(LeafApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(LeafApplication.class, args);
        log.info("(♥◠‿◠)ﾉﾞ  data-hamal-service-leaf start success  ლ(´ڡ`ლ)ﾞ");
    }
}