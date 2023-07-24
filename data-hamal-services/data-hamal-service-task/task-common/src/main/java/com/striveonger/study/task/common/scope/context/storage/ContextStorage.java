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

    <T> boolean offerFirst(String key, T e);

    <T> boolean offerLast(String key, T e);

    <T> T pollFirst(String key);

    <T> T pollLast(String key);

    <T> T peekFirst(String key);

    <T> T peekLast(String key);

}