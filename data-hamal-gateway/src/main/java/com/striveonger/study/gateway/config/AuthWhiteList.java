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
@ConfigurationProperties(prefix = "data-hamal.auth")
public class AuthWhiteList {

    private List<String> whites;

    public List<String> list() {
        return whites;
    }

    public List<String> getWhites() {
        return whites;
    }

    public void setWhites(List<String> whites) {
        this.whites = whites;
    }
}
