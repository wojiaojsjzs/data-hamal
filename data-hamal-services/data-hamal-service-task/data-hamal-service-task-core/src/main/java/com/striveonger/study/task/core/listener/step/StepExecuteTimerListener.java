package com.striveonger.study.task.core.listener.step;

import com.striveonger.study.task.common.listener.Listener;
import com.striveonger.study.task.common.scope.context.StepContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-05-16 17:01
 */
public class StepExecuteTimerListener implements Listener {
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
    public void error(StepContext context) {
        long time = timer.stop(key(context));
        log.info("Step '{}' elapsed time: {}ms", context.getDisplayName(), time);
    }

    private String key(StepContext context) {
        return String.format("%d_%s", context.getTaskID(), context.getStepID());
    }

    /**
     * 执行计时器
     */
    private static class ExecuteTimer {
        Map<String, Long> starts = new ConcurrentHashMap<>();
        /**
         * 开始计时
         * @param key : taskID_stepID
         */
        public void start(String key) {
            // starts.put(key, System.nanoTime());
            starts.put(key, System.currentTimeMillis());
        }

        /**
         * 停止计时
         * @param key : taskID_stepID
         * @return
         */
        public long stop(String key) {
            Long start = starts.remove(key);
            // return System.nanoTime() - (start == null ? 0 : start);
            return System.currentTimeMillis()- (start == null ? 0 : start);

        }
    }
}
