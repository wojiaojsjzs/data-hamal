package com.striveonger.study.gateway.config;

import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.URI;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-03-03 10:38
 */
@Configuration
public class GatewayConfig {
    private final Logger log = LoggerFactory.getLogger(GatewayConfig.class);

    @Resource
    private AuthWhiteList whites;

    /**
     * 添加用户登录的过滤器
     */
    @Bean
    public SaReactorFilter loginReactorFilter() {
        return new SaReactorFilter()
                // 设置拦截地址
                .addInclude("/**")
                // 加入白名单
                .setExcludeList(whites.list())
                // 鉴权方法: 每次访问进入
                .setAuth(o -> {
                    // 登录校验 -- 拦截所有路由
                    SaRouter.match("/**").check(x -> StpUtil.checkLogin());
                })
                // 处理未登录的请求
                .setError(e -> {
                    // 抛出异常, 统一交由 ExceptionHandler 处理
                    throw new CustomException(ResultStatus.NEED_USER_LOGIN, e.getMessage());
                });
    }


    /**
     * User相关的自定义路由规则
     */
    @Bean
    public RouteLocator userCustomRouteRule(RouteLocatorBuilder builder) {
        // 路由构造器
        RouteLocatorBuilder.Builder routes = builder.routes();
        // 设置路径(请求转发)
        routes.route("login_custom_routh", r -> r.path("/login").filters(fn -> fn.rewritePath("/login", "/auth/user/login")).uri("lb://data-hamal-auth"));
        routes.route("logout_custom_routh", r -> r.path("/logout").filters(fn -> fn.rewritePath("/logout", "/auth/user/logout")).uri("lb://data-hamal-auth"));
        routes.route("register_custom_routh", r -> r.path("/register").filters(fn -> fn.rewritePath("/register", "/auth/user/register")).uri("lb://data-hamal-auth"));
        return routes.build();
    }



}
