package com.striveonger.study.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Mr.Lee
 * @description: 全局缓存获取body请求数据（解决流不能重复读取问题）
 * @date 2023-03-02 15:06
 */
@Component
public class GlobalCacheRequestFilter implements GlobalFilter, Ordered {
    private final Logger log = LoggerFactory.getLogger(GlobalCacheRequestFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // GET DELETE 不过滤
        HttpMethod method = exchange.getRequest().getMethod();
        if (method == null || method == HttpMethod.GET || method == HttpMethod.DELETE) {
            return chain.filter(exchange);
        }
        return ServerWebExchangeUtils.cacheRequestBodyAndRequest(exchange, (request) -> {
            if (request == exchange.getRequest()) {
                return chain.filter(exchange);
            }
            return chain.filter(exchange.mutate().request(request).build());
        });
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE + 998;
    }
}
