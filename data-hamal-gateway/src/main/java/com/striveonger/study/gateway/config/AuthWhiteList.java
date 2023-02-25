package com.striveonger.study.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author Mr.Lee
 * @description: 权限验证的白名单
 * @date 2023-02-22 17:51
 */
@Configuration
@ConfigurationProperties(prefix = "auth.white")
public class AuthWhiteList {

    private List<String> list;

    public List<String> getList() {
        return list;
    }

    public List<String> list() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }



}
