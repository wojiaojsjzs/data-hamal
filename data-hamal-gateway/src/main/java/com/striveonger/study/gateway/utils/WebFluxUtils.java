package com.striveonger.study.gateway.utils;

import com.striveonger.study.core.result.Result;
import com.striveonger.study.core.utils.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Mr.Lee
 * @description: 写回自定义内容
 * @date 2023-02-24 17:35
 */
public class WebFluxUtils {
    private final static Logger log = LoggerFactory.getLogger(WebFluxUtils.class);

    /**
     * Response 写回自定义内容
     * @param exchange 请求体
     * @param result   相应的消息
     * @return
     */
    public static <T> Mono<Void> responseWriter(ServerWebExchange exchange, Result<T> result) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(HttpStatus.OK);
        log.error("处理异常, 请求路径: {}", exchange.getRequest().getPath());
        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory buffer = response.bufferFactory();
            byte[] bytes = JacksonUtils.toJSONBytes(result);
            return buffer.wrap(bytes);
        }));
    }

}
