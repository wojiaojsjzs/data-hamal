package com.striveonger.study.api.leaf.constant;

import static com.striveonger.study.api.leaf.constant.Keys.Type.*;

/**
 * @author Mr.Lee
 * @description: 模块对应的key(定义新的key时, 也要在数据库中进行同步定义)
 * @date 2023-03-08 15:23
 */
public enum Keys {

    // data-hamal-auth
    AUTH_USER("auth.user", SEGMENT, 1, 1, "用户ID"),
    FILE_STORAGE("file.storage", SNOWFLAKE, 1, 1, "文件ID");
    // data-hamal-auth

    private final String key;
    private final Type type;
    private final Integer start;
    private final Integer step;
    private final String description;

    Keys(String key, Type type, Integer start, Integer step, String description) {
        this.key = key;
        this.type = type;
        this.start = start;
        this.step = step;
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public Integer getStart() {
        return start;
    }

    public Integer getStep() {
        return step;
    }

    public String getDescription() {
        return description;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        /**
         * 号段模式
         */
        SEGMENT,
        /**
         * 雪花模式
         */
        SNOWFLAKE;
    }
}
