package com.striveonger.study.api.leaf;

import com.striveonger.study.api.leaf.core.ID;

/**
 * @author Mr.Lee
 * @description: Leaf服务(ID服务), 对外开放的RPC调用接口
 * @date 2023-03-07 16:09
 */
public interface IDGenRemoteService {

    ID get(String key);
}
