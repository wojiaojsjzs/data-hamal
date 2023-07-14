package com.striveonger.study.task.core.listener.step;

import com.striveonger.study.task.common.StepListener;
import com.striveonger.study.task.common.scope.context.StepContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mr.Lee
 * @description: 执行时间监听器
 * @date 2023-05-16 17:01
 */
public class StepExecuteTimerListener implements StepListener {
    private final Logger log = LoggerFactory.getLogger(StepExecuteTimerListener.class);

    private final ExecuteTimer timer = new ExecuteTimer();

    @Override
    public void before(StepContext context) {
        timer.start(key(context));
    }

    @Override
    public void after(StepContext context) {
        long time = timer.stop(key(context));
        log.info("Step '{}' elapsed time: {}ms", context.getDisplayName(), time);
    }

    @Override
    public void error(StepContext context, Exception e) {
        long time = timer.stop(key(context));
        log.error("Step '{}' elapsed time: {}ms", context.getDisplayName(), time);
    }

    @Override
    public boolean need(StepContext context) {
        // 全部组件都需要添加的监听器
        return true;
    }

    @Override
    public Priority getPriority() {
        return Priority.MAX;
    }

    private String key(StepContext context) {
        return String.format("%s_%s", context.getTaskID(), context.getStepID());
    }

    /**
     * 执行计时器
     */
    private static class ExecuteTimer {
        Map<String, Long> starts = new ConcurrentHashMap<>();

        /**
         * 开始计时
         *
         * @param key : taskID_stepID
         */
        public void start(String key) {
            // starts.put(key, System.nanoTime());
            starts.put(key, System.currentTimeMillis());
        }

        /**
         * 停止计时
         *
         * @param key : taskID_stepID
         * @return
         */
        public long stop(String key) {
            Long start = starts.remove(key);
            // 毫秒级
            return System.currentTimeMillis() - (start == null ? 0 : start);
            // 纳秒级
            // return System.nanoTime() - (start == null ? 0 : start);
        }
    }
}
