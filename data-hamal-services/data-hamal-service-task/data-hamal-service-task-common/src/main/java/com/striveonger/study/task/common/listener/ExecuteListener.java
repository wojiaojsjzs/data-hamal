package com.striveonger.study.task.common.listener;

import java.lang.annotation.*;

/**
 * 只作用于 {@link Listener} 的实现类
 * @author Mr.Lee
 * @description: 执行监听器
 * @date 2023-04-23 16:51
 */
@Documented                          // 定义可以被文档工具文档化
@Retention(RetentionPolicy.RUNTIME)  // 声明周期为runtime，运行时可以通过反射拿到
@Target(ElementType.TYPE)            // 注解修饰范围为类、接口、枚举
public @interface ExecuteListener {

    /**
     * 监听类型(监听的作用域)
     */
    Listener.Type type();

    /**
     * 监听顺序
     */
    int order() default 10;
}
