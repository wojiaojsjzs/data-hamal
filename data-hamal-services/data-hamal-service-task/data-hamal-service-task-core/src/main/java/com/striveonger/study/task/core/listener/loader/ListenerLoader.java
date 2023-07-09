package com.striveonger.study.task.core.listener.loader;

import com.striveonger.study.task.common.Listener;

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

}
