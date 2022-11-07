package com.striveonger.study.files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Mr.Lee
 * @description:
 * @date 2022-11-06 23:31
 */
@SpringBootApplication
@ComponentScan({"com.striveonger.study.*"})
public class FileApplication {

    private static final Logger log = LoggerFactory.getLogger(FileApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(FileApplication.class, args);
        log.info("(♥◠‿◠)ﾉﾞ  data-hamal-service-files start success  ლ(´ڡ`ლ)ﾞ");
    }
}