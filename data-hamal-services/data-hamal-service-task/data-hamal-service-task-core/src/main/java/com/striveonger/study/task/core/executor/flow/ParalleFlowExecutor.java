package com.striveonger.study.task.core.executor.flow;

import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import com.striveonger.study.task.core.executor.Executable;
import com.striveonger.study.task.core.listener.Listener;
import com.striveonger.study.task.core.scope.Workbench;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author Mr.Lee
 * @description: 并行执行器
 * @date 2022-11-28 22:12
 */
public class ParalleFlowExecutor extends FlowExecutor {
    private final Logger log = LoggerFactory.getLogger(ParalleFlowExecutor.class);

    @Override
    public void execute() {
        Workbench.Worker worker = workbench.worker();
        try {
            log.info("Paralle Flow Exec Start...");
            List<Executable> tasks = this.subtasks;
            // 1.CountDownLatch
            CountDownLatch latch = new CountDownLatch(tasks.size());
            for (Executable task : tasks) {
                worker.work(() -> {
                    try {
                        task.run();
                        latch.countDown();
                    } catch (Exception e) {
                        log.error("Paralle Executor execute failure...", e);
                        throw new CustomException(ResultStatus.TASK_EXECUTE_FAIL);
                    }
                });
            }
            latch.await();
            // latch.await(60 * 3, TimeUnit.SECONDS);

            // Thread.join
            // List<Thread> threads = new ArrayList<>();
            // for (Executable task : tasks) {
            //     threads.add(new Thread(task));
            // }
            // threads.forEach(Thread::start);
            // for (Thread t : threads) {
            //     t.join();
            // }

            // 3. Semaphore
            // Semaphore semaphore = new Semaphore(0);
            // for (Executable task : tasks) {
            //     worker.work(() -> {
            //         try {
            //             task.run();
            //             semaphore.release();
            //         } catch (Exception e) {
            //             log.error("Paralle Executor execute failure...", e);
            //             throw new CustomException(ResultStatus.TASK_EXECUTE_FAIL);
            //         }
            //     });
            // }
            // semaphore.acquire(tasks.size());
            log.info("Paralle Flow Exec Finish...");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}