package com.striveonger.study.task.core.executor.flow;

import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import com.striveonger.study.task.core.executor.Executable;
import com.striveonger.study.task.core.scope.Workbench;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author Mr.Lee
 * @description: 并行执行器
 * @date 2022-11-28 22:12
 */
public class ParalleFlowExecutor extends FlowExecutor {
    private final Logger log = LoggerFactory.getLogger(ParalleFlowExecutor.class);

    public ParalleFlowExecutor(Workbench workbench) {
        super(workbench);
    }

    @Override
    public void execute() throws Exception {
        Workbench.Worker worker = workbench.getWorker();
        try {
            List<Executable> tasks = this.subtasks;
            // 1. CountDownLatch
            CountDownLatch latch = new CountDownLatch(tasks.size());
            for (Executable task : tasks) {
                // TODO 后面考虑换成JDK19支持的虚拟线程...后面再说吧
                worker.work(() -> {
                    try {
                        task.run();
                    } catch (Exception e) {
                        log.error("Paralle Executor execute failure...", e);
                        throw e;
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();
            // latch.await(60 * 3, TimeUnit.SECONDS);

            // 2. Thread.join
            // List<Thread> threads = new ArrayList<>();
            // for (Executable task : tasks) {
            //     // 这样创建的线程, 不受线程池的约束...不好不好
            //     threads.add(new Thread(task));
            // }
            // threads.forEach(Thread::start);
            // for (Thread t : threads) t.join();

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
        } catch (Exception e) {
            if (e instanceof CustomException) throw e;
            throw new CustomException(ResultStatus.TASK_EXECUTE_FAIL);
        }
    }
}