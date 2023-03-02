package com.striveonger.study.gateway.filter;

import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import com.striveonger.study.core.utils.JacksonUtils;
import com.striveonger.study.gateway.config.AuthWhiteList;
import com.striveonger.study.gateway.decorator.LoginDecorator;
import com.striveonger.study.gateway.utils.URLMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-02-22 17:22
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {
    private final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    private final String USER_LOGIN = "/auth/user/login";

    @Resource
    private AuthWhiteList whites;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();


            // 不验证白名单中的URL(不验证用户信息的URL)
            String visitURL = request.getURI().getPath();
            log.info("Request URL {}", visitURL);
            if (URLMatch.match(visitURL, whites.list())) {
                if (USER_LOGIN.equals(visitURL)) {
                    MediaType mediaType = exchange.getRequest().getHeaders().getContentType();
                    if (MediaType.APPLICATION_JSON.equals(mediaType) || MediaType.APPLICATION_FORM_URLENCODED.equals(mediaType)) {
                        return DataBufferUtils.join(exchange.getRequest().getBody()).flatMap(dataBuffer -> {
                            byte[] bytes = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(bytes);
                            DataBufferUtils.release(dataBuffer);
                            String username, s = new String(bytes, StandardCharsets.UTF_8);
                            if (MediaType.APPLICATION_JSON.equals(mediaType)) {
                                // JSON 格式的请求
                                Map<String, Object> body = JacksonUtils.toMap(s);
                                // 用户登录, 需要抓出username来
                                username = Optional.ofNullable(body).map(x -> x.get("username")).map(Object::toString).orElse("");
                            } else {
                                // 解析请求体中的参数
                                MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
                                String[] pairs = s.split("&");
                                for (String pair : pairs) {
                                    String[] parts = pair.split("=");
                                    String name = parts[0];
                                    String value = parts.length > 1 ? parts[1] : null;
                                    formData.add(name, value);
                                }
                                username = formData.getFirst("username");
                            }
                            if (StrUtil.isNotBlank(username)) {
                                LoginDecorator decorator = new LoginDecorator(exchange, username);
                                return chain.filter(exchange.mutate().response(decorator).build());
                            }
                            return chain.filter(exchange);
                        });
                    }
                }
                return chain.filter(exchange);
            }
            // SaToken 检查用户是否登录
            StpUtil.checkLogin();
            return chain.filter(exchange);
        } catch (SaTokenException e) {
            log.error("用户认证失败", e);
            throw new CustomException(ResultStatus.NEED_USER_LOGIN, "登录超时");
        } catch (Exception e) {
            log.error("接口调用失败", e);
            throw new CustomException(ResultStatus.ACCIDENT, e.getMessage());
        }
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE + 999;
    }
}
