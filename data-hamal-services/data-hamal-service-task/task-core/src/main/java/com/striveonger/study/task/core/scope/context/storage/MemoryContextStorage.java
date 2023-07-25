package com.striveonger.study.task.core.scope.context.storage;

import cn.hutool.core.util.StrUtil;
import com.striveonger.study.task.common.scope.context.storage.ContextStorage;
import org.checkerframework.checker.units.qual.C;
import org.checkerframework.checker.units.qual.K;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.AbstractList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

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
    public  <T> T get(String key, Class<T> clazz) {
        if (containsKey(key)) {
            try {
                Object o = map.get(key);
                if (clazz.isInstance(o)) {
                    return (T) o;
                }
            } catch (Exception e) {
                log.error("Type must match...", e);
            }
        }
        return null;
    }

    @Override
    public void remove(String key) {
        map.remove(key);
    }

    @Override
    public <T> void offerFirst(String key, T value) {
        if (StrUtil.isNotBlank(key)) {
            deque(key, list -> list.offerFirst(value));
        }
    }

    @Override
    public <T> void offerLast(String key, T value) {
        if (StrUtil.isNotBlank(key) || Objects.nonNull(value)) {
            deque(key, list -> list.offerLast(value));
        }
    }

    @Override
    public <T> T pollFirst(String key, Class<T> clazz) {
        return deque(key, list -> {
            Object o = list.pollFirst();
            if (clazz.isInstance(o)) {
                return o;
            }
            return null;
        });
    }

    @Override
    public <T> T pollLast(String key, Class<T> clazz) {
        return deque(key, list -> {
            Object o = list.pollLast();
            if (clazz.isInstance(o)) {
                return o;
            }
            return null;
        });
    }

    @Override
    public <T> T peekFirst(String key, Class<T> clazz) {
        return deque(key, list -> {
            Object o = list.peekFirst();
            if (clazz.isInstance(o)) {
                return o;
            }
            return null;
        });
    }

    @Override
    public <T> T peekLast(String key, Class<T> clazz) {
        return deque(key, list -> {
            Object o = list.peekLast();
            if (clazz.isInstance(o)) {
                return o;
            }
            return null;
        });
    }

    private <T> T deque(String key, Function<LinkedList<Object>, Object> action) {
        synchronized (key.intern()) {
            if (!containsKey(key)) {
                map.put(key, new LinkedList<Object>());
            }
            LinkedList<Object> list = get(key, LinkedList.class);
            if (Objects.nonNull(list)) {
                Object o = action.apply(list);
                return (T) o;
            }
            return null;
        }
    }
}
