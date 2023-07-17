package com.striveonger.study.task.plugin.entity;

import com.striveonger.study.task.common.entity.BuildConfig;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-07-17 15:40
 */
public class TableOutputBuildConfig extends BuildConfig {
    private String driverPath;

    private String driverClassName;

    private String url;

    private String username;

    private String password;

    private String tableName;

    private boolean nonExistCreate;

    private String updateType;

    public String getDriverPath() {
        return driverPath;
    }

    public void setDriverPath(String driverPath) {
        this.driverPath = driverPath;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public boolean isNonExistCreate() {
        return nonExistCreate;
    }

    public void setNonExistCreate(boolean nonExistCreate) {
        this.nonExistCreate = nonExistCreate;
    }

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }


}