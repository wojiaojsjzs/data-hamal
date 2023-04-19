package com.striveonger.study.leaf.remote;

import com.striveonger.study.api.leaf.IDGenRemoteService;
import com.striveonger.study.api.leaf.core.ID;
import com.striveonger.study.leaf.core.IDGen;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private IDGen segmentGen;

    @Resource
    private IDGen snowflakeGen;

    @Override
    public ID acquireSerial(String key) {
        log.info("Remote Create Serial ID by key {}", key);
        return segmentGen.get(key);
    }

    @Override
    public ID acquireDisrupt(String key) {
        log.info("Remote Create Disrupt ID by key {}", key);
        return snowflakeGen.get(key);
    }
}
