package com.striveonger.study.task.common.scope.status;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mr.Lee
 * @description: 执行状态控制对象
 * @date 2023-07-11 15:11
 */
public interface StatusControls {

    /**
     * 任务开始
     *
     * @param id   任务ID
     * @param size 任务包含的步骤数量
     */
    void start(String id, int size);

    /**
     * 任务结束
     *
     * @param id 任务ID
     */
    void stop(String id);

    /**
     * 获取任务状态
     *
     * @param id 任务ID
     * @return   状态对象
     */
    RuntimeStatus get(String id);

    /**
     * 默认使用"本地内存"存储任务的执行状态
     */
    public static class Default implements StatusControls {

        private final Map<String, RuntimeStatus> cache = new ConcurrentHashMap<>();

        @Override
        public void start(String id, int size) {
            synchronized (id.intern()) {
                if (!cache.containsKey(id)) {
                    RuntimeStatus status = new RuntimeStatus(size);
                    cache.put(id, status);
                }
            }
        }

        @Override
        public void stop(String id) {
            cache.remove(id);
        }

        @Override
        public RuntimeStatus get(String id) {
            return cache.get(id);
        }
    }

    public static class Holder {
        private static final Holder instance = new Holder();

        private final StatusControls controls;

        private Holder() {
            ServiceLoader<StatusControls> loader = ServiceLoader.load(StatusControls.class);
            // 尝试加载外部实现, 如果没有加载到外部实现类, 则使用默认的
            this.controls  = loader.findFirst().orElseGet(Default::new);
        }

        /**
         * 获得控制器
         */
        public static StatusControls getControls() {
            return instance.controls;
        }
    }
}
