package com.striveonger.study.task.common.scope.context;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-05-04 16:00
 */
public class PerformParam {

    /**
     * 参数名
     */
    private String name;

    /**
     * 参数值 TODO: 后期是考虑支持SpringEL表达式 (少年, 未来可期~, 我再提一嘴呀...表达式的话 alterable 一定为false了)
     */
    private String value;

    /**
     * 运行时是否可变
     */
    private final boolean alterable;

    public PerformParam() {
        this.alterable = false;
    }

    /**
     * 默认为定义常量
     *
     * @param name
     * @param value
     */
    public PerformParam(String name, String value) {
        this(name, value, false);
    }

    public PerformParam(String name, String value, boolean alterable) {
        this.name = name;
        this.value = value;
        this.alterable = alterable;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isAlterable() {
        return alterable;
    }
}