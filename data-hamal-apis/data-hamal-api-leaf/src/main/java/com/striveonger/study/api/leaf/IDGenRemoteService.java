package com.striveonger.study.api.leaf;

import com.striveonger.study.api.leaf.core.ID;

/**
 * @author Mr.Lee
 * @description: Leaf服务(ID服务), 对外开放的RPC调用接口
 * @date 2023-03-07 16:09
 */
public interface IDGenRemoteService {

    /**
     * 获取有序的ID
     * @param key
     * @return
     */
    ID acquireSerial(String key);


    /**
     * 获取分散的ID
     * @param key
     * @return
     */
    ID acquireDisrupt(String key);
}
