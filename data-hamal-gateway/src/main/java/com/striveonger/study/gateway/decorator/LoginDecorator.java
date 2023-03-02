package com.striveonger.study.gateway.decorator;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.result.Result;
import com.striveonger.study.core.utils.JacksonUtils;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author Mr.Lee
 * @description: 登录装饰器
 * @date 2023-03-01 21:44
 */
// @Deprecated
public class LoginDecorator extends ServerHttpResponseDecorator {
    private final Logger log = LoggerFactory.getLogger(LoginDecorator.class);

    private final DataBufferFactory factory;
    private final String username;

    public LoginDecorator(ServerWebExchange exchange, String username) {
        super(exchange.getResponse());
        this.factory = exchange.getResponse().bufferFactory();
        this.username = username;
    }

    /**
     * 在装饰器里, 处理返回的数据
     * @param body
     * @return
     */
    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        // 进到这的, 肯定没有登录过的用户
        if (body instanceof Flux) {
            Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
            return super.writeWith(fluxBody.map(buffer -> {
                byte[] content = new byte[buffer.readableByteCount()];
                buffer.read(content);
                DataBufferUtils.release(buffer);
                String s = new String(content, StandardCharsets.UTF_8);
                Map<String, Object> result = JacksonUtils.toMap(s);
                byte[] tc = null; // treatedContent 处理过的内容
                if (result != null && ResultStatus.SUCCESS.toString().equals(result.get("code"))) {
                    // 执行登录动作(经过验证, 这种方式无法实现登录)
                    StpUtil.login(username);
                    SaTokenInfo token = StpUtil.getTokenInfo();
                    result.put("token", "xxxx");
                    tc = JacksonUtils.toJSONBytes(result);
                }
                return factory.wrap(tc == null ? content : tc);
            }));
        }
        return super.writeWith(body);
    }
}
