package com.striveonger.study.task.core;


import cn.hutool.core.thread.ThreadUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.striveonger.study.core.utils.JacksonUtils;
import com.striveonger.study.core.utils.SleepHelper;
import com.striveonger.study.task.common.constant.TaskStatus;
import com.striveonger.study.task.common.scope.context.PerformParam;
import com.striveonger.study.task.core.executor.Executor;
import com.striveonger.study.task.core.executor.extra.ExecutorExtraInfo;
import com.striveonger.study.task.core.launch.TaskLaunch;
import com.striveonger.study.task.core.scope.trigger.TaskTrigger;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
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


    @Test
    public void hello() {
        System.out.println("Hello Task...");
    }

    @Test
    public void test() {
        String json = """
            {
                "A1" : ["A2"],
            	"A2" : ["C"],
            	"B"  : ["C"],
            	"C"  : ["D", "E1", "F"],
            	"D"  : ["G"],
            	"E1" : ["E2"],
            	"E2" : ["G"],
            	"F"  : ["H"],
            	"G"  : ["I"],
            	"H"  : ["I"],
            	"I"  : ["J"],
            	"J"  : ["K"],
            	"K"  : []
            }
            """;

        test(json);
    }

    @Test
    public void test1() {
        String json = """
            {
                "G"  : ["A1"],
                "H"  : ["A1"],
                "I"  : ["A2"],
                "J"  : ["A2"],
                "A1" : ["B"],
            	"A2" : ["B"],
            	"B"  : []
            }
            """;

        test(json);
    }

    @Test
    public void test2() {
        String json = """
            {
                "B" : ["A1", "A2"],
            	"A1": ["G", "H"],
            	"A2": ["I", "J"]
            }
            """;
        test(json);
    }


    public void test(String json) {
        log.info("Test Start...");

        // 1. 初始化任务触发器
        TaskTrigger trigger = new TaskTrigger();
        trigger.setTaskID(20140527010300001L);

        // 2. 定义 DAG 任务
        int waitTimeConstant = 1;
        // 2.1 初始化任务列表
        Executor A1 = new TestExecutor("A1", waitTimeConstant);
        ExecutorExtraInfo A1e = new ExecutorExtraInfo();
        A1e.setStepID("A1");
        A1e.setDisplayName("A1");
        A1e.setExecutor(A1);
        trigger.putExtra(A1e);
        Executor A2 = new TestExecutor("A2", waitTimeConstant);
        ExecutorExtraInfo A2e = new ExecutorExtraInfo();
        A2e.setStepID("A2");
        A2e.setDisplayName("A2");
        A2e.setExecutor(A2);
        trigger.putExtra(A2e);
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
        Executor D = new TestExecutor("D", 2);
        ExecutorExtraInfo De = new ExecutorExtraInfo();
        De.setStepID("D");
        De.setDisplayName("D");
        De.setExecutor(D);
        trigger.putExtra(De);
        Executor E1 = new TestExecutor("E1", waitTimeConstant);
        ExecutorExtraInfo E1e = new ExecutorExtraInfo();
        E1e.setStepID("E1");
        E1e.setDisplayName("E1");
        E1e.setExecutor(E1);
        trigger.putExtra(E1e);
        Executor E2 = new TestExecutor("E2", waitTimeConstant);
        ExecutorExtraInfo E2e = new ExecutorExtraInfo();
        E2e.setStepID("E2");
        E2e.setDisplayName("E2");
        E2e.setExecutor(E2);
        trigger.putExtra(E2e);
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
        Map<String, Set<String>> topology = JacksonUtils.toObject(json, new TypeReference<>() { });
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
    public void testMap() {
        Map<Long, String> map = new HashMap<>();
        map.put(1L, "A");
        map.put(2L, "B");
        map.put(3L, "C");
        map.put(4L, "D");
        Set<Long> set = new HashSet<>(map.keySet());
        set.remove(1L);
        String s = map.get(1L);
        System.out.println(s);

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
            // if ("D".equals(this.name)) {
            //     throw new RuntimeException();
            // }
            SleepHelper.sleepMilliSeconds(waitTime * 500);
        }

        public String getName() {
            return name;
        }
    }
}