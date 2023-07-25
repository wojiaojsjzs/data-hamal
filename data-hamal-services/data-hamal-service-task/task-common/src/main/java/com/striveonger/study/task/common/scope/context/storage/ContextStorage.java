package com.striveonger.study.task.common.scope.context.storage;

/**
 * @author Mr.Lee
 * @description: Context 存储器
 * @date 2023-07-20 13:45
 */
public interface ContextStorage {

    boolean containsKey(String key);

    void put(String key, Object value);

    <T> T get(String key, Class<T> clazz);

    void remove(String key);

    <T> void offerFirst(String key, T e);

    <T> void offerLast(String key, T e);

    <T> T pollFirst(String key, Class<T> clazz);

    <T> T pollLast(String key, Class<T> clazz);

    <T> T peekFirst(String key, Class<T> clazz);

    <T> T peekLast(String key, Class<T> clazz);

    default String getString(String key) {
        return get(key, String.class);
    }

    default Integer getInteger(String key) {
        return get(key, Integer.class);
    }

    default Long getLong(String key) {
        return get(key, Long.class);
    }
}