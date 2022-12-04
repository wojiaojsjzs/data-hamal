package com.striveonger.study.task.core.flow;

import com.striveonger.study.task.core.Executable;
import com.striveonger.study.task.core.WorkArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * @author Mr.Lee
 * @description: 并行执行器
 * @date 2022-11-28 22:12
 */
public class ParalleFlow extends BasicExecutable {
    private final Logger log = LoggerFactory.getLogger(ParalleFlow.class);

    @Override
    public void exec() {
        WorkArea workArea = this.getWorkArea();
        WorkArea.Worker worker = workArea.getWorker();
        // TODO: 执行前要前检查任务状态
        try {
            log.info("Paralle Flow Exec Start...");
            List<Executable> tasks = this.subtasks;
            CountDownLatch latch = new CountDownLatch(tasks.size());
            for (Executable task : tasks) {
                worker.work(() -> {
                    try {
                        task.run();
                        latch.countDown();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            latch.await(200, TimeUnit.SECONDS);
            log.info("Paralle Flow Exec Finish...");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}