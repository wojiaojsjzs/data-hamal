package com.striveonger.study.hello.remote;

import com.striveonger.study.api.hello.HelloRemoteService;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-03-09 14:05
 */
@DubboService
public class HelloRemoteServiceImpl implements HelloRemoteService {
    private final Logger log = LoggerFactory.getLogger(HelloRemoteServiceImpl.class);

    @Override
    public String hi(String name) {
        log.info("Hello Remote Service,  hi {}", name);
        return name + ", hi~";
    }
}
