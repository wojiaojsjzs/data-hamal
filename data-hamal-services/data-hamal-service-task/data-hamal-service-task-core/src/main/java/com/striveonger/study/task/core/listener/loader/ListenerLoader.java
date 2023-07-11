package com.striveonger.study.task.core.listener.loader;

import com.striveonger.study.task.common.Listener;
import com.striveonger.study.task.common.scope.context.StepContext;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-07-08 16:02
 */
public class ListenerLoader {

    private final List<Listener> listeners = new ArrayList<>();
    private final static ListenerLoader instance = new ListenerLoader();

    private ListenerLoader() {
        ServiceLoader<Listener> loader = ServiceLoader.load(Listener.class);
        for (Listener item : loader) {
            listeners.add(item);
        }
    }

    public static ListenerLoader getInstance() {
        return instance;
    }

    /**
     * 获取所有已注册的监听器
     */
    public Listener[] getFullRegisterListeners() {
        return listeners.toArray(Listener[]::new);
    }

    /**
     * 根据条件, 获取组件所需的监听器
     * 比如: 调试时运行时, 需要通过WebSocket向页面, 向页面输出执行日志. 而正式运行时, 则不需要
     *
     * @param context
     * @return
     */
    public Listener[] getListenersByConditions(StepContext context) {
        // 根据当前的执行环境, 过滤条件
        return listeners.stream().filter(l -> l.need(context)).toArray(Listener[]::new);
    }

}
