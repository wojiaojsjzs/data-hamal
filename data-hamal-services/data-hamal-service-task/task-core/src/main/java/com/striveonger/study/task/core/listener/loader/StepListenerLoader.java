package com.striveonger.study.task.core.listener.loader;

import com.striveonger.study.task.common.listener.Listener;
import com.striveonger.study.task.common.listener.StepListener;
import com.striveonger.study.task.common.scope.context.StepContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-07-08 16:02
 */
public class StepListenerLoader {

    private final List<StepListener> listeners = new ArrayList<>();
    private final static StepListenerLoader instance = new StepListenerLoader();

    private StepListenerLoader() {
        ServiceLoader<StepListener> loader = ServiceLoader.load(StepListener.class);
        for (StepListener item : loader) {
            listeners.add(item);
        }
    }

    public static StepListenerLoader getInstance() {
        return instance;
    }

    /**
     * 获取所有已注册的监听器
     */
    public StepListener[] getFullRegisterListeners() {
        return listeners.stream().sorted(Comparator.comparingInt(Listener::order).reversed()).toArray(StepListener[]::new);
    }

    /**
     * 根据条件, 获取组件所需的监听器
     * 比如: 调试时运行时, 需要通过WebSocket向页面, 向页面输出执行日志. 而正式运行时, 则不需要
     *
     * @param context
     * @return
     */
    public StepListener[] getListenersByConditions(StepContext context) {
        // 根据当前的执行环境, 过滤条件
        return listeners.stream().filter(l -> l.need(context)).sorted(Comparator.comparingInt(Listener::order).reversed()).toArray(StepListener[]::new);
    }

}
