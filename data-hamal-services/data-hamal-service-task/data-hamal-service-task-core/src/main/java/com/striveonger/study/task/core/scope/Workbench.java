package com.striveonger.study.task.core.scope;

import com.striveonger.study.task.core.exception.BuildTaskException;
import com.striveonger.study.task.core.executor.Executable;
import com.striveonger.study.task.core.executor.Executor;
import com.striveonger.study.task.core.executor.flow.FlowExecutor;
import com.striveonger.study.task.core.listener.Listener;
import com.striveonger.study.task.core.scope.context.RuntimeContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Mr.Lee
 * @description: 工作台
 * @date 2022-11-29 12:01
 */
public class Workbench {
    private final Logger log = LoggerFactory.getLogger(Workbench.class);

    /**
     * 任务的工作区
     * 包括:
     *   1. 任务的基本信息
     *   2. 启动参数(任务常量)
     *   3. 公共数据(执行变量)
     *   4. 上下文数据(组件产生的数据)
     *   5. 线程池(任务执行器, 每个任务只能有一个)
     */



    /**
     * 任务ID
     */
    private final long taskID;

    private final RuntimeContext context;

    /**
     * 天选打工人 & 职业经理人
     */
    private final Worker worker;

    /**
     * 为任务创建工作台
     *
     * @param taskID          任务ID
     * @param corePoolSize    常驻线程数
     * @param maximumPoolSize 最大线程数
     * @param keepAliveTime   非核心线程空闲时长(销毁)
     * @param unit            时间单位
     * @param workQueue       任务的等待队列
     * @param threadFactory   线程的创建器
     * @param handler         拒绝策略
     */
    private Workbench(long taskID, RuntimeContext context,
                      Integer corePoolSize, Integer maximumPoolSize,
                      Long keepAliveTime, TimeUnit unit,
                      BlockingQueue<Runnable> workQueue,
                      ThreadFactory threadFactory,
                      RejectedExecutionHandler handler) {
        this.taskID = taskID;
        this.context = context;
        this.worker = new Worker(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }


    public Worker getWorker() {
        return worker;
    }

    public long getTaskID() {
        return taskID;
    }

    public RuntimeContext getContext() {
        return context;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Task执行线程工厂
     */
    private static class TaskThreadFactory implements ThreadFactory {

        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        public TaskThreadFactory(long taskID) {
            namePrefix = "task-exec-" + taskID + "-thread-";
        }

        @Override
        public Thread newThread(Runnable runnable) {
            Thread t = new Thread(runnable, namePrefix + threadNumber.getAndIncrement());
            // 非守护线程
            if (t.isDaemon()) t.setDaemon(false);
            // 默认优先级
            if (t.getPriority() != Thread.NORM_PRIORITY) t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }

    /**
     * 工作者
     */
    public static class Worker {
        private final ExecutorService taskExecThreadPool;
        public Worker(Integer corePoolSize, Integer maximumPoolSize, Long keepAliveTime, TimeUnit unit,
                      BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
            this.taskExecThreadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        }
        public void work(Runnable runnable) {
            taskExecThreadPool.execute(runnable);
        }
    }

    public static class Builder {
        private Long taskID;
        private RuntimeContext context;
        private Integer corePoolSize = 8, maximumPoolSize = 32;
        private Long keepAliveTime = 30L;
        private final TimeUnit unit = TimeUnit.MILLISECONDS;
        // 核心线程满了, 任务会进入等待队列(workQueue). 当等待队列满了之后, 创建非核心线程来该执行任务.
        // private BlockingQueue<Runnable> workQueue = new SynchronousQueue<>();
        private BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>(1);
        private ThreadFactory threadFactory;
        // 线程池满了, 直接抛异常
        private RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();

        public Builder taskID(long id) {
            this.taskID = id;
            this.threadFactory = new TaskThreadFactory(id);
            return this;
        }

        public Builder context(RuntimeContext context) {
            this.context = context;
            return this;
        }

        public Builder corePoolSize(int size) {
            this.corePoolSize = size;
            return this;
        }

        public Builder maximumPoolSize(int size) {
            this.maximumPoolSize = size;
            return this;
        }

        public Builder keepAliveTime(long mills) {
            this.keepAliveTime = mills;
            return this;
        }

        public Builder workQueueSize(int size) {
            // 设置等待队列大小时, 一定要小心. 填不满队列的情况下, 有可能造成任务的整体阻塞
            this.workQueue = new LinkedBlockingQueue<>(size);
            return this;
        }

        public Builder handler(RejectedExecutionHandler handler) {
            this.handler = handler;
            return this;
        }

        public Workbench build() {
            if (taskID == null || context == null) throw new BuildTaskException(BuildTaskException.Type.WORKBENCH);
            return new Workbench(taskID, context, corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        }
    }
}
