package com.striveonger.study.hello.web.controller;

import com.striveonger.study.api.hello.HelloRemoteService;
import com.striveonger.study.core.result.Result;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-03-09 15:18
 */
@RestController
public class HelloController {
    private final Logger log = LoggerFactory.getLogger(HelloController.class);

    @DubboReference
    private HelloRemoteService service;

    @GetMapping("/hi")
    public Result<Object> hi(String name) {
        String hi = service.hi(name);
        return Result.success(hi);
    }


}
