package com.striveonger.study.hello.web;

import com.striveonger.study.core.utils.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-08-11 18:04
 */
@Controller
public class HelloController {
    private final Logger log = LoggerFactory.getLogger(HelloController.class);

    @PostMapping("/test")
    @ResponseBody
    public String test(@RequestBody Map<String, Object> map) {
        log.info("request data: " + JacksonUtils.toJSONString(map));
        return "ok";
    }

    



}
