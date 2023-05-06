package com.striveonger.study.task.core;


import cn.hutool.core.thread.ThreadUtil;
import com.striveonger.study.core.utils.SleepHelper;
import com.striveonger.study.task.core.executor.Executor;
import com.striveonger.study.task.core.executor.flow.ParalleFlowExecutor;
import com.striveonger.study.task.core.executor.flow.SerialeFlowExecutor;
import com.striveonger.study.task.core.listener.Listener;
import com.striveonger.study.task.core.listener.step.StepLogListener;
import com.striveonger.study.task.core.listener.task.TaskThreadNameListener;
import com.striveonger.study.task.core.scope.Workbench;
import com.striveonger.study.task.core.scope.context.RuntimeContext;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Mr.Lee
 * @description: 工作区的测试类
 * @date 2022-11-30 11:01
 */
public class WorkerTest {

    private final static Logger log = LoggerFactory.getLogger(WorkerTest.class);

    String json = """
            {
            	"A": ["C"],
            	"B": ["C"],
            	"C": ["D", "E", "F"],
            	"D": ["G"],
            	"E": ["G"],
            	"F": ["H"],
            	"G": ["I"],
            	"H": ["I"],
            	"I": ["J"],
            	"J": ["K"],
            	"K": []
            }
            """;

    @Test
    public void hello() {
        System.out.println("Hello Task...");
    }

    @Test
    public void test() {

        // 1. 初始化运行时环境
        // 2. 初始化listener
        // 3. 初始化工作空间
        // 4. 初始化 StepExecutor(设置 "运行时环境", "listener", "工作空间" )
        // 5. 生成 FlowExecutor
        // 6. 启动 MasterExecutor



        log.info("Test Start...");

        // 1. 初始化运行时环境
        RuntimeContext cxt = new RuntimeContext(1L, 11, null);
        // 2. 初始化工作空间
        Workbench workbench = Workbench.builder().taskID(1L).corePoolSize(1).maximumPoolSize(64).context(cxt).build();
        // Workbench.Worker worker = workbench.getWorker();
        // 3. 初始化listener
        Listener[] listeners = new Listener[]{new StepLogListener()};

        // 手动定义 DAG 任务
        int waitTimeConstant = 1;

        Executor A = new TestExecutor("A", waitTimeConstant);
        A.setListeners(listeners);
        Executor B = new TestExecutor("B", waitTimeConstant);
        B.setListeners(listeners);
        Executor C = new TestExecutor("C", waitTimeConstant);
        C.setListeners(listeners);
        Executor D = new TestExecutor("D", 5);
        D.setListeners(listeners);
        Executor E = new TestExecutor("E", waitTimeConstant);
        E.setListeners(listeners);
        Executor F = new TestExecutor("F", waitTimeConstant);
        F.setListeners(listeners);
        Executor G = new TestExecutor("G", waitTimeConstant);
        G.setListeners(listeners);
        Executor H = new TestExecutor("H", waitTimeConstant);
        H.setListeners(listeners);
        Executor I = new TestExecutor("I", waitTimeConstant);
        I.setListeners(listeners);
        Executor J = new TestExecutor("J", waitTimeConstant);
        J.setListeners(listeners);
        Executor K = new TestExecutor("K", waitTimeConstant);
        K.setListeners(listeners);

        ParalleFlowExecutor AB_P = new ParalleFlowExecutor();
        AB_P.setWorkbench(workbench);
        AB_P.push(A);
        AB_P.push(B);

        SerialeFlowExecutor ABC_S = new SerialeFlowExecutor();
        ABC_S.setWorkbench(workbench);
        ABC_S.push(AB_P);
        ABC_S.push(C);

        SerialeFlowExecutor FH_S = new SerialeFlowExecutor();
        FH_S.setWorkbench(workbench);
        FH_S.push(F);
        FH_S.push(H);

        ParalleFlowExecutor DE_P = new ParalleFlowExecutor();
        DE_P.setWorkbench(workbench);
        DE_P.push(D);
        DE_P.push(E);

        SerialeFlowExecutor DEG_S = new SerialeFlowExecutor();
        DEG_S.setWorkbench(workbench);
        DEG_S.push(DE_P);
        DEG_S.push(G);

        ParalleFlowExecutor DEG_FH_P = new ParalleFlowExecutor();
        DEG_FH_P.setWorkbench(workbench);
        DEG_FH_P.push(DEG_S);
        DEG_FH_P.push(FH_S);

        SerialeFlowExecutor master = new SerialeFlowExecutor();
        master.setWorkbench(workbench);
        master.setListeners(new Listener[]{new TaskThreadNameListener(), new StepLogListener()});
        master.push(ABC_S);
        master.push(DEG_FH_P);
        master.push(I);
        master.push(J);
        master.push(K);

        // 开始工作～
        Thread current = Thread.currentThread();
        String oldName = current.getName();
        String taskMasterThreadName = String.format("task-exec-%d-master", workbench.getTaskID());
        current.setName(taskMasterThreadName);
        master.execute();
        log.info("Test End...");
        current.setName(oldName);
    }

    @Test
    public void testPool() throws Exception {
        ExecutorService service = Executors.newCachedThreadPool();
        int[] i = new int[1];
        CountDownLatch latch = new CountDownLatch(20);
        for (i[0] = 0; i[0] < 20; i[0]++) {
            service.execute(() -> {
                int x = i[0];
                ThreadUtil.sleep(400);
                log.info("hello {}", x);
                latch.countDown();
            });
        }
        log.info("Thread Pool");
        latch.await();
    }

    @Test
    public void testWorker() throws Exception {
        Workbench workbench = Workbench.builder().taskID(1024L).corePoolSize(0).maximumPoolSize(Integer.MAX_VALUE).build();
        Workbench.Worker worker = workbench.getWorker();
        int[] i = new int[1];
        CountDownLatch latch = new CountDownLatch(20);
        for (i[0] = 0; i[0] < 20; i[0]++) {
            worker.work(() -> {
                int x = i[0];
                ThreadUtil.sleep(400);
                log.info("hello {}", x);
                latch.countDown();
            });
        }
        log.info("Thread Pool");
        latch.await();
    }

    @Test
    public void testMap() {
        Map<Long, String> map = new HashMap<>();
        map.put(1L, "A");
        map.put(2L, "B");
        map.put(3L, "C");
        map.put(4L, "D");
        long x = 2;
        System.out.println(map.get(x));
    }

    private static class TestExecutor extends Executor {

        private final String name;
        private final int waitTime;

        public TestExecutor(String name, int waitTime) {
            this.name = name;
            this.waitTime = waitTime;
        }

        @Override
        public void execute() throws Exception {
            log.info("start {}", name);
            SleepHelper.sleepSeconds(waitTime);
            log.info("  end {}", name);
        }
    }

}