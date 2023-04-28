package com.striveonger.study.test.logs;

import ch.qos.logback.classic.*;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;

import java.nio.charset.StandardCharsets;


/**
 * @author Mr.Lee
 * @description: 自定义Logger配置, 代替logback.xml文件
 * @date 2023-02-27 17:58
 */
public class CustomConfigurator extends BasicConfigurator {

    private final Level level = Level.INFO;

    @Override
    public void configure(LoggerContext ctx) {
        addInfo("Setting up custom configuration.");
        Logger root = ctx.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(level);
        root.addAppender(consoleAppender(ctx));
    }

    private Appender<ILoggingEvent> consoleAppender(LoggerContext ctx) {
        ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<>();
        appender.setContext(ctx);
        appender.setName("CONSOLE");

        Encoder<ILoggingEvent> encoder = buildEncoder(ctx);
        appender.setEncoder(encoder);

        appender.start();
        return appender;
    }

    private Encoder<ILoggingEvent> buildEncoder(LoggerContext ctx) {
        PatternLayout layout = new PatternLayout();
        layout.setPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} %level [%thread] %logger{36}:%line %msg%n");
        layout.setContext(ctx);
        layout.start();

        LayoutWrappingEncoder<ILoggingEvent> encoder = new LayoutWrappingEncoder<>();
        encoder.setContext(ctx);
        encoder.setLayout(layout);
        encoder.setCharset(StandardCharsets.UTF_8);
        encoder.start();
        return encoder;
    }
}
