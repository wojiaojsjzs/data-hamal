package com.striveonger.study.task.core;


import cn.hutool.core.thread.ThreadUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.striveonger.study.core.utils.JacksonUtils;
import com.striveonger.study.core.utils.SleepHelper;
import com.striveonger.study.task.core.constant.TaskStatus;
import com.striveonger.study.task.core.executor.Executor;
import com.striveonger.study.task.core.executor.extra.ExecutorExtraInfo;
import com.striveonger.study.task.core.launch.TaskLaunch;
import com.striveonger.study.task.core.scope.Workbench;
import com.striveonger.study.task.core.scope.trigger.PerformParam;
import com.striveonger.study.task.core.scope.trigger.TaskTrigger;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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

        // 1.
        // 2. 初始化listener
        // 3. 初始化工作空间
        // 4. 初始化 StepExecutor(设置 "运行时环境", "listener", "工作空间" )
        // 5. 生成 FlowExecutor
        // 6. 启动 MasterExecutor

        log.info("Test Start...");

        // 1. 初始化任务触发器
        TaskTrigger trigger = new TaskTrigger();
        trigger.setTaskID(1L);

        // 2. 定义 DAG 任务
        int waitTimeConstant = 1;
        // 2.1 初始化任务列表
        Executor A = new TestExecutor("A", waitTimeConstant);
        ExecutorExtraInfo Ae = new ExecutorExtraInfo();
        Ae.setStepID("A");
        Ae.setDisplayName("A");
        Ae.setExecutor(A);
        trigger.putExtra(Ae);
        Executor B = new TestExecutor("B", waitTimeConstant);
        ExecutorExtraInfo Be = new ExecutorExtraInfo();
        Be.setStepID("B");
        Be.setDisplayName("B");
        Be.setExecutor(B);
        trigger.putExtra(Be);
        Executor C = new TestExecutor("C", waitTimeConstant);
        ExecutorExtraInfo Ce = new ExecutorExtraInfo();
        Ce.setStepID("C");
        Ce.setDisplayName("C");
        Ce.setExecutor(C);
        trigger.putExtra(Ce);
        Executor D = new TestExecutor("D", 5);
        ExecutorExtraInfo De = new ExecutorExtraInfo();
        De.setStepID("D");
        De.setDisplayName("D");
        De.setExecutor(D);
        trigger.putExtra(De);
        Executor E = new TestExecutor("E", waitTimeConstant);
        ExecutorExtraInfo Ee = new ExecutorExtraInfo();
        Ee.setStepID("E");
        Ee.setDisplayName("E");
        Ee.setExecutor(E);
        trigger.putExtra(Ee);
        Executor F = new TestExecutor("F", waitTimeConstant);
        ExecutorExtraInfo Fe = new ExecutorExtraInfo();
        Fe.setStepID("F");
        Fe.setDisplayName("F");
        Fe.setExecutor(F);
        trigger.putExtra(Fe);
        Executor G = new TestExecutor("G", waitTimeConstant);
        ExecutorExtraInfo Ge = new ExecutorExtraInfo();
        Ge.setStepID("G");
        Ge.setDisplayName("G");
        Ge.setExecutor(G);
        trigger.putExtra(Ge);
        Executor H = new TestExecutor("H", waitTimeConstant);
        ExecutorExtraInfo He = new ExecutorExtraInfo();
        He.setStepID("H");
        He.setDisplayName("H");
        He.setExecutor(H);
        trigger.putExtra(He);
        Executor I = new TestExecutor("I", waitTimeConstant);
        ExecutorExtraInfo Ie = new ExecutorExtraInfo();
        Ie.setStepID("I");
        Ie.setDisplayName("I");
        Ie.setExecutor(I);
        trigger.putExtra(Ie);
        Executor J = new TestExecutor("J", waitTimeConstant);
        ExecutorExtraInfo Je = new ExecutorExtraInfo();
        Je.setStepID("J");
        Je.setDisplayName("J");
        Je.setExecutor(J);
        trigger.putExtra(Je);
        Executor K = new TestExecutor("K", waitTimeConstant);
        ExecutorExtraInfo Ke = new ExecutorExtraInfo();
        Ke.setStepID("K");
        Ke.setDisplayName("K");
        Ke.setExecutor(K);
        trigger.putExtra(Ke);

        // 2.2 定义任务的拓扑序
        // Map<String, Set<String>> topology = new HashMap<>();
        // topology.put("A", Set.of("C"));
        // topology.put("B", Set.of("C"));
        // topology.put("C", Set.of("D", "E", "F"));
        // topology.put("D", Set.of("G"));
        // topology.put("E", Set.of("G"));
        // topology.put("F", Set.of("H"));
        // topology.put("G", Set.of("I"));
        // topology.put("H", Set.of("I"));
        // topology.put("I", Set.of("J"));
        // topology.put("J", Set.of("K"));
        // topology.put("K", Set.of());
        Map<String, Set<String>> topology = JacksonUtils.toObject(json, new TypeReference<Map<String, Set<String>>>() {});
        trigger.setTopology(topology);

        // 3. 定义任务参数
        trigger.putParam(new PerformParam("a", "1", true));
        trigger.putParam(new PerformParam("b", "2", true));
        trigger.putParam(new PerformParam("c", "3"));


        // 4. 定义任务启动器, 并启动
        TaskLaunch launch = new TaskLaunch(trigger);
        TaskStatus status = launch.start();
        log.info("task execute stauts " + status);
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