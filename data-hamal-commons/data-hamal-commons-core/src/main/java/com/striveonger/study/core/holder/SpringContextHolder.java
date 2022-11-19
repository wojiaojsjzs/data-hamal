package com.striveonger.study.core.holder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author Mr.Lee
 * @description:
 * @date 2022-11-05 15:07
 */
@Component
public class SpringContextHolder implements ApplicationContextAware {
    private final Logger log = LoggerFactory.getLogger(SpringContextHolder.class);

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        SpringContextHolder.context = context;
    }

    public static ApplicationContext getContext() {
        assertContext();
        return context;
    }

    public static <T> T getBean(String beanName) {
        assertContext();
        return (T) context.getBean(beanName);
    }

    public static <T> T getBean(Class<T> clazz) {
        assertContext();
        return context.getBean(clazz);
    }

    public static <T> T getBean(String beanName, Class<T> clazz) {
        assertContext();
        return context.getBean(beanName, clazz);
    }

    public static Environment getEnvironment() {
        assertContext();
        return context.getEnvironment();
    }

    private static void assertContext() {
        if (SpringContextHolder.context == null) {
            throw new RuntimeException("applicaitonContext属性为null, 请检查是否注入了SpringContextHolder!");
        }
    }


}
