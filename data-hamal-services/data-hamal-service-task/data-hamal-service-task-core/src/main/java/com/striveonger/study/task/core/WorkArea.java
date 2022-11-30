package com.striveonger.study.task.core;

import com.striveonger.study.task.core.constant.ExecutionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Mr.Lee
 * @description: 工作区
 * @date 2022-11-29 12:01
 */
public class WorkArea {

    private final Logger log = LoggerFactory.getLogger(WorkArea.class);

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

    private final Worker worker;

    private final Integer corePoolSize = 4;
    private final Integer maximumPoolSize = 16;
    private final Long keepAliveTime = 30L;
    private final TimeUnit unit = TimeUnit.SECONDS;
    private final BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(4);
    private final ThreadFactory threadFactory = new TaskThreadFactory();
    private final RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();

    private final ExecutorService service = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);


    public WorkArea(Long taskID) {
        this.taskID = taskID;
        this.status = ExecutionStatus.NONE.getCode();
        this.worker = new Worker(service);
    }

    public Worker getWorker() {
        return worker;
    }

    public void updateStatus(ExecutionStatus status) {
        synchronized (taskID.toString().intern()) {
            this.status = status.getCode();
        }
    }

    private class TaskThreadFactory implements ThreadFactory {

        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        public TaskThreadFactory() {
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


    public static class Worker {
        ExecutorService service;

        public Worker(ExecutorService service) {
            this.service = service;
        }

        public void work(Runnable runnable) {
            service.submit(runnable);
        }
    }

}
