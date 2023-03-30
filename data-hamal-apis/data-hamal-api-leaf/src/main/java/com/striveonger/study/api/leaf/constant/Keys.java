package com.striveonger.study.api.leaf.constant;

/**
 * @author Mr.Lee
 * @description: 模块对应的key(定义新的key时, 也要在数据库中进行同步定义)
 * @date 2023-03-08 15:23
 */
public enum Keys {

    // data-hamal-auth
    AUTH_USER("auth.user", 1, 1, "用户ID");
    // data-hamal-auth

    private final String key;
    private final Integer start;
    private final Integer step;
    private final String description;

    Keys(String key, Integer start, Integer step, String description) {
        this.key = key;
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

}
