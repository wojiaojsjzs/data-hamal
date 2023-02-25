package com.striveonger.study.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.striveonger.study.core.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.stream.DoubleStream;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-02-24 10:42
 */
public class JacksonUtils {

    private static final Logger log = LoggerFactory.getLogger(JacksonUtils.class);
    private static final ObjectMapper mapper;

    static {
        mapper = JsonMapper.builder()
                           .addModule(new JavaTimeModule())
                           .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                           .build();
    }

    public static String toJSONString(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error("Jackson Convert String error ", e);
        }
        return null;
    }

    public static byte[] toJSONBytes(Object o) {
        String str = toJSONString(o);
        return str == null ? null : str.getBytes(StandardCharsets.UTF_8);
    }
}
