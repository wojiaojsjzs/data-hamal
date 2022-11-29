package com.striveonger.study.task.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
     *   1. 启动参数(任务常量)
     *   2. 公共数据(执行变量)
     *   3. 上下文数据(组件产生的数据)
     *   4. 线程池(任务执行器, 每个任务只能有一个)
     */
    private ExecutorService service = new ThreadPoolExecutor(1, 16,
            30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>()),
    )







}
