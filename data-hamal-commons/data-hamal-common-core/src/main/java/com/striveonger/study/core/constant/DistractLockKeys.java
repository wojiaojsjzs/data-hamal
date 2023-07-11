package com.striveonger.study.core.constant;

/**
 * @author Mr.Lee
 * @description: 分布式锁统一管理的Key(不能重复哦~)
 * @date 2023-04-03 21:15
 */
public enum DistractLockKeys {
    INITIALIZE_LEAF_KEY("initialize_leaf_key");

    private final String key;

    DistractLockKeys(String key) {
        this.key = key;
    }

    public String key() {
        return key.intern();
    }
}
