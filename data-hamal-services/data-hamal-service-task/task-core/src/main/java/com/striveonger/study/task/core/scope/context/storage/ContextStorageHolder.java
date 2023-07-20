package com.striveonger.study.task.core.scope.context.storage;

import com.striveonger.study.task.common.scope.context.storage.ContextStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ServiceLoader;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-07-20 18:17
 */
public class ContextStorageHolder {
    private final Logger log = LoggerFactory.getLogger(ContextStorageHolder.class);

    private static final ContextStorageHolder instance = new ContextStorageHolder();

    private final ContextStorage storage;

    private ContextStorageHolder() {
        ServiceLoader<ContextStorage> loader = ServiceLoader.load(ContextStorage.class);
        this.storage = loader.findFirst().orElseGet(MemoryContextStorage::new);
    }

    public ContextStorage getStorage() {
        return storage;
    }

    public static ContextStorageHolder getInstance() {
        return instance;
    }

}