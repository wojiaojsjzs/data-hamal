package com.striveonger.study.task.plugin.entity;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.striveonger.study.task.common.entity.BuildConfig;

/**
 * @author Mr.Lee
 * @description: 组件基础配置
 * @date 2023-07-14 14:35
 */
@JsonTypeName("SQL_INPUT")
public class SQLInputBuildConfig extends BuildConfig {

    private String driverPath;

    private String driverClassName;

    private String url;

    private String username;

    private String password;

    private String sql;

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

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
