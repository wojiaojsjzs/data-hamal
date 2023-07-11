package com.striveonger.study.task.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-04-19 16:15
 */
@SpringBootApplication
@ComponentScan({"com.striveonger.study.*"})
public class TaskWorkerApplication {
    private static final Logger log = LoggerFactory.getLogger(TaskWorkerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TaskWorkerApplication.class, args);
        log.info("(♥◠‿◠)ﾉﾞ  data-hamal-service-task-worker start success  ლ(´ڡ`ლ)ﾞ");
    }
}
