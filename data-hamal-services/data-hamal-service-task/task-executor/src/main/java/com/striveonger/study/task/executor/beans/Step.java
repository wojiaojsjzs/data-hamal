package com.striveonger.study.task.executor.beans;

/**
 * @author Mr.Lee
 * @description: 步骤实体
 * @date 2023-07-14 14:35
 */
public class Step {

    /**
     * 步骤ID
     */
    private String id;

    /**
     * 步骤名
     */
    private String displayName;
    /**
     * 组件类型
     */
    private String type;

    /**
     * 组件构建所需的内容(*每个组件差异也就在这了*)
     */
    private BuildConfig buildConfig;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BuildConfig getBuildConfig() {
        return buildConfig;
    }

    public void setBuildConfig(BuildConfig buildConfig) {
        this.buildConfig = buildConfig;
    }
}
