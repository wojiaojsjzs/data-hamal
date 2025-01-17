package com.striveonger.study.task.core;

import com.striveonger.study.task.core.constant.ExecutionStatus;
import com.striveonger.study.task.core.exception.BuildTaskException;
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

    /*
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
    private final Long taskID;

    /**
     * 任务执行状态
     */
    private volatile int status;

    /**
     * 天选打工人
     */
    private final Worker worker;


    /**
     * 为任务创建工作台
     *
     * @param taskID          任务ID
     * @param status          任务执行状态
     * @param corePoolSize    常驻线程数
     * @param maximumPoolSize 最大线程数
     * @param keepAliveTime   非核心线程空闲时长(销毁)
     * @param unit            时间单位
     * @param workQueue       任务的等待队列
     * @param threadFactory   线程的创建器
     * @param handler         拒绝策略
     */
    public Workbench(Long taskID, int status,
                     Integer corePoolSize, Integer maximumPoolSize,
                     Long keepAliveTime, TimeUnit unit,
                     BlockingQueue<Runnable> workQueue,
                     ThreadFactory threadFactory,
                     RejectedExecutionHandler handler) {
        this.taskID = taskID;
        this.status = status;
        this.worker = new Worker(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }


    public Worker worker() {
        return worker;
    }

    public int status() {
        return status;
    }

    public void updateStatus(ExecutionStatus status) {
        synchronized (taskID.toString().intern()) {
            this.status = status.getCode();
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private static class TaskThreadFactory implements ThreadFactory {

        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        public TaskThreadFactory(Long taskID) {
            namePrefix = "task-exec-" + taskID + "-thread-";
        }

        @Override
        public Thread newThread(Runnable runnable) {
            Thread t = new Thread(runnable, namePrefix + threadNumber.getAndIncrement());
            if (t.isDaemon()) t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY) t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }


    /**
     * 工作者
     */
    public static class Worker {

        private final Integer corePoolSize;
        private final Integer maximumPoolSize;
        private final Long keepAliveTime;
        private final TimeUnit unit;
        private final BlockingQueue<Runnable> workQueue;
        private final ThreadFactory threadFactory;
        private final RejectedExecutionHandler handler;

        private final ExecutorService taskExecThreadPool;

        public Worker(Integer corePoolSize, Integer maximumPoolSize, Long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.keepAliveTime = keepAliveTime;
            this.unit = unit;
            this.workQueue = workQueue;
            this.threadFactory = threadFactory;
            this.handler = handler;

            this.taskExecThreadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        }

        public void work(Executable executable) {
            taskExecThreadPool.submit(executable);
        }
    }

    public static class Builder {
        private Long taskID;
        private Integer status = ExecutionStatus.NONE.getCode();
        private Integer corePoolSize = 8, maximumPoolSize = 32;
        private Long keepAliveTime = 30L;
        private final TimeUnit unit = TimeUnit.SECONDS;
        private BlockingQueue<Runnable> workQueue = new SynchronousQueue<>();
        private ThreadFactory threadFactory;
        private RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();

        public Builder taskID(long id) {
            this.taskID = id;
            this.threadFactory = new TaskThreadFactory(id);
            return this;
        }

        public Builder status(ExecutionStatus status) {
            this.status = status.getCode();
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

        public Builder keepAliveTime(long time) {
            this.keepAliveTime = time;
            return this;
        }

        public Builder workQueueSize(int size) {
            this.workQueue = new LinkedBlockingQueue<>(size);
            return this;
        }

        public Builder handler(RejectedExecutionHandler handler ) {
            this.handler = handler;
            return this;
        }

        public Workbench build() {
            if (taskID == null) throw new BuildTaskException(BuildTaskException.Type.WORKBENCH);
            return new Workbench(taskID, status, corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        }


    }
}
