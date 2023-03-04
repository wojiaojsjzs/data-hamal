package com.striveonger.study.logs;

import ch.qos.logback.classic.*;
import ch.qos.logback.classic.filter.ThresholdFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;

import java.nio.charset.StandardCharsets;

/**
 * @author Mr.Lee
 * @description: 自定义Logger配置, 代替logback.xml文件
 * @date 2023-02-27 17:58
 */
public class CustomConfigurator extends BasicConfigurator {

    private final Level level = Level.DEBUG;

    @Override
    public void configure(LoggerContext ctx) {
        addInfo("Setting up custom configuration.");
        Logger root = ctx.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(level);
        root.addAppender(consoleAppender(ctx));
        // root.addAppender(fileConsoleAppender(ctx));
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

    private Appender<ILoggingEvent> fileConsoleAppender(LoggerContext ctx) {
        RollingFileAppender<ILoggingEvent> appender = new RollingFileAppender<>();
        appender.setContext(ctx);
        appender.setName("FILE_CONSOLE");

        // 这个路径是要计算的
        String filePath = "/Users/striveonger/development/workspace/temp/logs/console.log";
        appender.setFile(filePath);
        System.out.println(appender.rawFileProperty());

        Encoder<ILoggingEvent> encoder = buildEncoder(ctx);
        appender.setEncoder(encoder);

        TimeBasedRollingPolicy<ILoggingEvent> policy = new TimeBasedRollingPolicy();
        policy.setContext(ctx);
        policy.setFileNamePattern("/Users/striveonger/development/workspace/temp/logs/console.%d{yyyy-MM-dd}.log");
        policy.setParent(appender);
        policy.setMaxHistory(1);
        policy.start();
        appender.setRollingPolicy(policy);

        ThresholdFilter filter = new ThresholdFilter();
        filter.setContext(ctx);
        filter.setLevel(level.toString());
        filter.start();
        appender.addFilter(filter);

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
