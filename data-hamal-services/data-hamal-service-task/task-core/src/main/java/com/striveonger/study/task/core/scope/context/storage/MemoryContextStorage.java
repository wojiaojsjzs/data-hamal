package com.striveonger.study.task.core.scope.context.storage;

import com.striveonger.study.task.common.scope.context.storage.ContextStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-07-20 21:07
 */
public class MemoryContextStorage implements ContextStorage {
    private final Logger log = LoggerFactory.getLogger(MemoryContextStorage.class);

    Map<String, Object> map = new ConcurrentHashMap<>();

    @Override
    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    @Override
    public void put(String key, Object value) {
        map.put(key, value);
    }




    @Override
    public void remove(String key) {
        map.remove(key);
    }

    @Override
    public <T> T get(String key) {
        if (containsKey(key)) {
            return (T) map.get(key);
        }
        return null;
    }
}
