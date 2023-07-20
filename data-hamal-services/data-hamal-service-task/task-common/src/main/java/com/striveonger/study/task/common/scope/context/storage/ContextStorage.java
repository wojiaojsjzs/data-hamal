package com.striveonger.study.task.common.scope.context.storage;

/**
 * @author Mr.Lee
 * @description: Context 存储器
 * @date 2023-07-20 13:45
 */
public interface ContextStorage {

    boolean containsKey(String key);

    void put(String key, Object value);

    void remove(String key);

    <T> T get(String key);

}