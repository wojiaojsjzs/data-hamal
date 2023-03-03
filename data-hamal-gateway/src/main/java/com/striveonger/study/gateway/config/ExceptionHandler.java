package com.striveonger.study.gateway.config;

import com.striveonger.study.core.exception.CustomException;
import com.striveonger.study.core.result.Result;
import com.striveonger.study.gateway.utils.WebFluxUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Mr.Lee
 * @description: 统一的错误处理器
 * @date 2023-02-24 17:19
 */
@Configuration
@Order(Integer.MIN_VALUE)
public class ExceptionHandler implements ErrorWebExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(ExceptionHandler.class);

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable t) {
        ServerHttpResponse response = exchange.getResponse();
        if (response.isCommitted()) {
            return Mono.error(t);
        }
        Result<Object> message;
        if (t instanceof CustomException e) {
             message = Result.status(e.getStatus()).message(e.getMessage());
        } else {
            message = Result.fail().message(t.getMessage());
        }
        log.error("处理失败的请求. ", t);
        return WebFluxUtils.responseWriter(exchange, message);
    }
}
