package com.striveonger.study.api.leaf.constant;

/**
 * @author Mr.Lee
 * @description: 模块对应的key(定义新的key时, 也要在数据库中进行同步定义)
 * @date 2023-03-08 15:23
 */
public interface Keys {
    // data-hamal-auth
    String AUTH_USER = "auth.user";
    // data-hamal-auth
    // TODO: 改动计划:
    //  1. 改为枚举的形式
    //  2. 启动Leaf服务时, 检查是否为号段模式, 如果是, 检查库中是否已初始化该key, 没有的话, 要初始化
}
