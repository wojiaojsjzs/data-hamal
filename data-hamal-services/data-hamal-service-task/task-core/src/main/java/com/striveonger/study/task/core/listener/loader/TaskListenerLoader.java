package com.striveonger.study.task.core.listener.loader;

import com.striveonger.study.task.common.listener.Listener;
import com.striveonger.study.task.common.listener.TaskListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-07-08 16:02
 */
public class TaskListenerLoader {

    private final List<TaskListener> listeners = new ArrayList<>();
    private final static TaskListenerLoader instance = new TaskListenerLoader();

    private TaskListenerLoader() {
        ServiceLoader<TaskListener> loader = ServiceLoader.load(TaskListener.class);
        for (TaskListener item : loader) {
            listeners.add(item);
        }
    }

    public static TaskListenerLoader getInstance() {
        return instance;
    }

    /**
     * 获取所有已注册的监听器
     */
    public TaskListener[] getFullRegisterListeners() {
        return listeners.stream().sorted(Comparator.comparingInt(Listener::order).reversed()).toArray(TaskListener[]::new);
    }

}
