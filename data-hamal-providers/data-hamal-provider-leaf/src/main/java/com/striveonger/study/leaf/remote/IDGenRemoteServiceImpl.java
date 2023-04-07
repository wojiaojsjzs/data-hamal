package com.striveonger.study.leaf.remote;

import com.striveonger.study.api.leaf.IDGenRemoteService;
import com.striveonger.study.api.leaf.core.ID;
import com.striveonger.study.leaf.core.IDGen;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-03-08 09:38
 */
@DubboService //(version = "1.0.0")
public class IDGenRemoteServiceImpl implements IDGenRemoteService {
    private final Logger log = LoggerFactory.getLogger(IDGenRemoteServiceImpl.class);

    @Resource
    private IDGen serialGen;

    @Resource
    private IDGen disruptGen;

    @Override
    public ID acquireSerial(String key) {
        log.info("Remote Create Serial ID by key {}", key);
        return serialGen.get(key);
    }

    @Override
    public ID acquireDisrupt(String key) {
        log.info("Remote Create Disrupt ID by key {}", key);
        return disruptGen.get(key);
    }
}
