package com.striveonger.study.gateway.filter;

import cn.dev33.satoken.stp.StpUtil;
import com.striveonger.study.core.result.Result;
import com.striveonger.study.core.utils.JacksonUtils;
import com.striveonger.study.gateway.config.AuthWhiteList;
import com.striveonger.study.gateway.utils.URLMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-02-22 17:22
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {
    private final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    @Resource
    private AuthWhiteList whites;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        // 不验证白名单中的URL(不验证用户信息的URL)
        String visitURL = request.getURI().getPath();
        log.info("Request URL {}", visitURL);
        if (URLMatch.match(visitURL, whites.list())) {
            return chain.filter(exchange);
        }
        // SaToken 检查用户是否登录
        StpUtil.checkLogin();

        return null;
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
