package com.striveonger.study.task.common.scope.status;

import com.striveonger.study.task.common.constant.StepStatus;
import com.striveonger.study.task.common.constant.TaskStatus;

import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mr.Lee
 * @description: 执行状态控制对象(RuntimeStatus的包装类)
 * 为什么不把状态控制器, 放到RuntimeContext中呢?
 * 考虑到后面的中止与暂停操作, 不是在任务执行中修改的...需要从外面通过任务ID就能修改到任务状态
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

    /* 包装开始 */

    /**
     * 更新任务的执行状态
     * @param id      任务ID
     * @param num     步骤索引
     * @param status  步骤执行状态
     */
    public void update(String id, int num, StepStatus status);

    public StepStatus stepStatus(String id, int num);

    public TaskStatus taskStatus(String id);

    /* 包装结束 */

    /**
     * 默认使用"本地内存"存储任务的执行状态
     */
    public static class Default implements StatusControls {

        private final Map<String, RuntimeStatus> cache = new ConcurrentHashMap<>();

        private Default() {  }

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
        public void update(String id, int num, StepStatus status) {
            synchronized (id.intern()) {
                RuntimeStatus holder = cache.get(id);
                if (Objects.nonNull(holder)) {
                    holder.update(num, status);
                }
            }
        }

        @Override
        public StepStatus stepStatus(String id, int num) {
            RuntimeStatus holder = cache.get(id);
            if (Objects.nonNull(holder)) {
                return holder.stepStatus(num);
            }
            return null;
        }

        @Override
        public TaskStatus taskStatus(String id) {
            RuntimeStatus holder = cache.get(id);
            if (Objects.nonNull(holder)) {
                return holder.taskStatus();
            }
            return null;
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
