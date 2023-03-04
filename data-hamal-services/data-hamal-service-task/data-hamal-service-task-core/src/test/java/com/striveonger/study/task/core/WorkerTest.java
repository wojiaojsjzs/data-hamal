package com.striveonger.study.task.core;


import com.striveonger.study.core.utils.SleepHelper;
import com.striveonger.study.task.core.flow.ParalleFlow;
import com.striveonger.study.task.core.flow.SerialeFlow;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

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

        log.info("Test Start...");

        Workbench workbench = Workbench.builder().taskID(1L).corePoolSize(0).build();
        Workbench.Worker worker = workbench.worker();

        // 手动定义 DAG 任务
        int waitTimeConstant = 1;

        BiConsumer<String, Integer> consumer = (s, waitTime) -> {
            log.info("start {}", s);
            SleepHelper.sleepSeconds(waitTime);
            log.info("  end {}", s);
        };

        Executable A = () -> consumer.accept("A", waitTimeConstant);
        Executable B = () -> consumer.accept("B", waitTimeConstant);
        Executable C = () -> consumer.accept("C", waitTimeConstant);
        Executable D = () -> consumer.accept("D", 30);
        Executable E = () -> consumer.accept("E", waitTimeConstant);
        Executable F = () -> consumer.accept("F", waitTimeConstant);
        Executable G = () -> consumer.accept("G", waitTimeConstant);
        Executable H = () -> consumer.accept("H", waitTimeConstant);
        Executable I = () -> consumer.accept("I", waitTimeConstant);
        Executable J = () -> consumer.accept("J", waitTimeConstant);
        Executable K = () -> consumer.accept("K", waitTimeConstant);

        SerialeFlow full = new SerialeFlow("full");
        full.setWorkbench(workbench);

        ParalleFlow AB_P = new ParalleFlow("AB_P");
        AB_P.setWorkbench(workbench);
        AB_P.push(A);
        AB_P.push(B);

        SerialeFlow ABC_S = new SerialeFlow("ABC_S");
        ABC_S.setWorkbench(workbench);
        ABC_S.push(AB_P);
        ABC_S.push(C);

        SerialeFlow FH_S = new SerialeFlow("FH_S");
        FH_S.setWorkbench(workbench);
        FH_S.push(F);
        FH_S.push(H);

        ParalleFlow DE_P = new ParalleFlow("DE_P");
        DE_P.setWorkbench(workbench);
        DE_P.push(D);
        DE_P.push(E);

        SerialeFlow DEG_S = new SerialeFlow("DEG_S");
        DEG_S.setWorkbench(workbench);
        DEG_S.push(DE_P);
        DEG_S.push(G);

        ParalleFlow DEG_FH_P = new ParalleFlow("DEG_FH_P");
        DEG_FH_P.setWorkbench(workbench);
        DEG_FH_P.push(DEG_S);
        DEG_FH_P.push(FH_S);

        full.push(ABC_S);
        full.push(DEG_FH_P);
        full.push(I);
        full.push(J);
        full.push(K);

        // 开始工作～
        worker.work(full);

        SleepHelper.sleepSeconds(60);
        log.info("Test End...");
    }

    @Test
    public void testPool() {
        ExecutorService service = Executors.newFixedThreadPool(1);
        service.submit(() -> System.out.println("hello"));
        log.info("Thread Pool");
    }


}