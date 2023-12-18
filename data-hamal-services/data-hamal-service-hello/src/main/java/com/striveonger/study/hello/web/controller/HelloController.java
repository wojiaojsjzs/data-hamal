package com.striveonger.study.hello.web.controller;

import cn.hutool.core.lang.Dict;
import com.striveonger.study.api.hello.HelloRemoteService;
import com.striveonger.study.core.result.Result;
import com.striveonger.study.rabbit.entity.RabbitMessage;
import com.striveonger.study.rabbit.hold.RabbitHold;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.function.Function;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-03-09 15:18
 */
@RestController
public class HelloController {
    private final Logger log = LoggerFactory.getLogger(HelloController.class);

    @DubboReference
    private HelloRemoteService service;


    @Resource
    private RabbitHold hold;

    @GetMapping("/hi")
    public Result hi(String name) {
        String hi = service.hi(name);
        return Result.success(hi);
    }

    @GetMapping("/test")
    public Result test() {
        Dict dict = Dict.create();
        log.info("test start...");
        String exchangeName = "data.hamal.exchange", queueName = "data.hamal.message.full", routingKey = "data.hamal.message.#";
        // 1. 交换机
        // 1.1 检查指定的交换机(Exchange)是否存在
        boolean checkExchangeExists = hold.checkExchangeExists(exchangeName);
        dict.set("checkExchangeExists", checkExchangeExists);
        // 1.2 如果交换机不存在, 则尝试创建交换机
        if (!checkExchangeExists) {
            // 尝试创建交换机
            boolean createExchange = hold.tryCreateExchange(exchangeName);
            dict.set("createExchange", createExchange);
        }
        // 2. 消息队列
        // 2.1 检查指定的消息队列(Queue)是否存在
        boolean checkQueueExists = hold.checkQueueExists(queueName);
        dict.put("checkQueueExists", checkQueueExists);
        // 2.2 如果消息队列不存在, 则尝试创建队列
        if (!checkQueueExists) {
            // 尝试创建队列
            boolean createQueue = hold.tryCreateQueue(queueName);
            dict.put("createQueue", createQueue);
        }
        // 3. 绑定交换机与消息队列
        if (!checkExchangeExists || !checkQueueExists) {
            boolean binding = hold.binding(exchangeName, queueName, routingKey);
            dict.set("binding", binding);
        }
        log.info("test end...");
        return Result.success().data(dict);
    }
    @GetMapping("/test/send")
    public Result testSend() {
        Dict dict = Dict.create();
        log.info("test start...");
        String exchangeName = "data.hamal.exchange", queueName = "data.hamal.message.full", routingKey = "data.hamal.message.#";

        // 4. 给队列中发送消息
        StringMessage message = new StringMessage();
        message.content("劝君更进一杯洒");
        boolean send = hold.send(exchangeName, routingKey, message);
        dict.set("send", send);

        log.info("test end...");

        return Result.success().data(dict);
    }
    @GetMapping("/test/receive")
    public Result testReceive() {
        Dict dict = Dict.create();
        log.info("test start...");
        String exchangeName = "data.hamal.exchange", queueName = "data.hamal.message.full", routingKey = "data.hamal.message.#";

        // 5. 从队列中接收消息
        Function<StringMessage, Boolean> function = message -> {
            dict.set("message", message.content());
            return true;
        };
        boolean receive = hold.receive(queueName, function, StringMessage.class);
        dict.set("receive", receive);
        log.info("test end...");

        return Result.success().data(dict);
    }

    public static class StringMessage implements RabbitMessage {
        private String content;

        public String content() {
            return content;
        }

        public void content(String content) {
            this.content = content;
        }
    }
}
