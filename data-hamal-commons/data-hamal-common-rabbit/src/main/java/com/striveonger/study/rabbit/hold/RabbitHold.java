package com.striveonger.study.rabbit.hold;

import com.rabbitmq.client.*;
import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import com.striveonger.study.rabbit.entity.RabbitMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-04-10 17:37
 */
public class RabbitHold {

    private final Logger log = LoggerFactory.getLogger(RabbitHold.class);

    private final RabbitTemplate template;

    public RabbitHold(RabbitTemplate template) {
        this.template = template;
    }

    /**
     * 检查交换机是否存在
     *
     * @param name
     * @return
     */
    public boolean checkExchangeExists(String name) {
        try (Connection connection = template.getConnectionFactory().createConnection();
             Channel channel = connection.createChannel(false)) {
            channel.exchangeDeclarePassive(name);
            log.info("exchange: \"{}\" existent", name);
            return true;
        } catch (IOException | TimeoutException e) {
            log.info("exchange: \"{}\" not existent", name, e);
        }
        return false;
    }

    /**
     * 尝试创建交换机
     *
     * @param name
     * @return
     */
    public boolean tryCreateExchange(String name) {
        try (Connection connection = template.getConnectionFactory().createConnection();
             Channel channel = connection.createChannel(false)) {
            // 创建一个"主题"类型, 并且持久化
            channel.exchangeDeclare(name, BuiltinExchangeType.TOPIC, true);
            return true;
        } catch (IOException | TimeoutException e) {
            log.error("create exchange \"{}\" failure", name, e);
        }
        return false;
    }


    /**
     * 检查队列是否存在
     *
     * @param name
     * @return
     */
    public boolean checkQueueExists(String name) {
        try (Connection connection = template.getConnectionFactory().createConnection();
             Channel channel = connection.createChannel(false)) {
            channel.queueDeclarePassive(name);
            return true;
        } catch (IOException | TimeoutException e) {
            log.info("exchange: \"{}\" not existent", name, e);
        }
        return false;
    }

    /**
     * 尝试创建队列
     *
     * @param name
     * @return
     */
    public boolean tryCreateQueue(String name) {
        try (Connection connection = template.getConnectionFactory().createConnection();
             Channel channel = connection.createChannel(false)) {
            channel.queueDeclare(name, true, false, false, null);
            return true;
        } catch (IOException | TimeoutException e) {
            log.error("create queue \"{}\" failure", name, e);
        }
        return false;
    }

    /**
     * 绑定交换机与队列
     *
     * @param exchangeName
     * @param queueName
     * @param routingKey
     * @return
     */
    public boolean binding(String exchangeName, String queueName, String routingKey) {
        try (Connection connection = template.getConnectionFactory().createConnection();
             Channel channel = connection.createChannel(false)) {
            channel.queueBind(queueName, exchangeName, routingKey);
            return true;
        } catch (IOException | TimeoutException e) {
            log.error("binding exchange: \"{}\", queue: \"{}\" failure, routingKey: \"{}\"", exchangeName, queueName, routingKey, e);
        }
        return false;
    }


    /**
     * 发送消息
     *
     * @param exchangeName
     * @param routingKey
     * @param message
     * @param <T>
     * @return
     */
    public <T extends RabbitMessage> boolean send(String exchangeName, String routingKey, T message) {
        return send(exchangeName, routingKey, message, false);
    }

    /**
     * 发送消息
     *
     * @param message
     * @param exchangeName
     * @param routingKey
     * @param throwable    是否会抛出异常
     * @param <T>
     * @return
     */
    public <T extends RabbitMessage> boolean send(String exchangeName, String routingKey, T message, boolean throwable) {
        // contentType：消息体的类型，如application/json、text/plain等。
        // contentEncoding：消息体的编码格式，如UTF-8、GBK等。
        // deliveryMode：消息的持久化方式，有两种值：1表示消息不持久化，2表示消息持久化。
        // expiration：消息的过期时间，单位为毫秒。
        // messageId：消息的唯一标识符。
        // timestamp：消息的时间戳。
        // type：消息的类型。
        // userId：消息的发送者ID。
        AMQP.BasicProperties props = MessageProperties.PERSISTENT_BASIC; // 只有交换机和队列设为持久化属性没用哦, 发送的消息也要持久化呦~
        try (Connection connection = template.getConnectionFactory().createConnection();
             Channel channel = connection.createChannel(false)) {
            // exchange：消息交换机的名称，表示消息将被发送到哪个交换机中。
            // routingKey：路由键，表示消息将被发送到哪个队列中。如果交换机与队列绑定时指定了路由键，则消息发送时需要指定相应的路由键。
            // mandatory：布尔值，表示消息是否需要被路由到至少一个队列中。如果设置为true，但是消息无法路由到任何队列，则会返回一个Basic.Return命令给生产者。如果设置为false，则会直接丢弃消息。
            // immediate：布尔值，表示消息是否需要立即被消费者处理。如果设置为true，但是没有消费者立即可用，则会返回一个Basic.Return命令给生产者。如果设置为false，则会将消息存入队列中等待消费者处理。
            // props：消息的属性，包括消息的类型、优先级、过期时间等。
            // body：消息的内容，为字节数组类型。
            channel.basicPublish(exchangeName, routingKey, true, false, props, serialize(message));
            return true;
        } catch (IOException | TimeoutException e) {
            log.error("send message: \"{}\" failure, exchange: \"{}\" routingKey: \"{}\"", message, exchangeName, routingKey, e);
            if (throwable) {
                throw new CustomException(ResultStatus.MESSAGE_SEND_FAIL);
            }
        }
        return false;
    }


    /**
     * 从指定队列中接取一条消息来处理
     *
     * @param queueName
     * @param dispose   消息处理逻辑
     */
    public <T extends RabbitMessage> boolean receive(String queueName, Function<T, Boolean> dispose, Class<T> clazz) {
        return receive(queueName, dispose, false, clazz);
    }


    /**
     * 从指定队列中接取一条消息来处理
     *
     * @param queueName
     * @param dispose   消息处理逻辑
     * @param blocked   是否会阻塞当前线程
     */
    public <T extends RabbitMessage> boolean receive(String queueName, Function<T, Boolean> dispose, boolean blocked, Class<T> clazz) {
        Message message;
        if (blocked) {
            message = template.receive(queueName);
        } else {
            message = template.receive(queueName, 500);
        }
        if (message != null) {
            try {
                long deliveryTag = message.getMessageProperties().getDeliveryTag();
                AtomicBoolean flag = new AtomicBoolean(false);
                try {
                    T msg = deserialize(message.getBody(), clazz);
                    flag.set(dispose.apply(msg));
                } catch (Exception e) {
                    log.error("message receive failure...", e);
                    flag.set(false);
                }

                template.execute(channel -> {
                    if (flag.get()) {
                        channel.basicAck(deliveryTag, false);
                        log.info("message receive success...");
                    } else {
                        // multiple 批量操作
                        // requeue  重新排队true, 丢弃, 则为 false
                        channel.basicNack(deliveryTag, false, true);
                        log.info("message tuìhuí success...");
                    }
                    return null;
                });
                return flag.get();
            } catch (Exception e) {
                log.error("message handle failure...", e);
            }
        }
        return false;
    }

    /**
     * 序列化对象
     *
     * @param message
     * @param <T>
     * @return
     */
    private <T extends RabbitMessage> byte[] serialize(T message) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(message);
            oos.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            log.error("message serialize failure...", e);
            throw new CustomException(ResultStatus.MESSAGE_SERIALIZE_FAIL);
        }

    }

    /**
     * 反序列化对象
     *
     * @param bytes
     * @param clazz
     * @param <T>
     * @return
     */
    private <T extends RabbitMessage> T deserialize(byte[] bytes, Class<T> clazz) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            Object obj = ois.readObject();
            ois.close();
            return clazz.cast(obj);
        } catch (IOException | ClassNotFoundException e) {
            log.error("message deserialize failure...", e);
            throw new CustomException(ResultStatus.MESSAGE_DESERIALIZATION_FAIL);
        }
    }
}
