package com.striveonger.study.task.core;


import com.striveonger.study.task.core.flow.ParalleFlow;
import com.striveonger.study.task.core.flow.SerialeFlow;
import com.striveonger.study.tools.SleepHelper;
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
            	"J": ["K"]
            }
            """;

    @Test
    public void hello() {
        System.out.println("Hello Task...");
    }

    @Test
    public void test() {
        Workbench workbench = Workbench.builder().taskID(1L).corePoolSize(0).build();
        Workbench.Worker worker = workbench.getWorker();

        // 手动定义 DAG 任务
        int waitTime = 1;

        BiConsumer<String, Integer> consumer = new BiConsumer<String, Integer>() {
            int waitTime = 1;

            @Override
            public void accept(String s, Integer waitTime) {
                log.info("start {}", s);
                SleepHelper.sleepSeconds(waitTime);
                log.info("  end {}", s);
            }
        };

        Executable A = () -> consumer.accept("A", waitTime);
        Executable B = () -> consumer.accept("B", waitTime);
        Executable C = () -> consumer.accept("C", waitTime);
        Executable D = () -> consumer.accept("D", 30);
        Executable E = () -> consumer.accept("E", waitTime);
        Executable F = () -> consumer.accept("F", waitTime);
        Executable G = () -> consumer.accept("G", waitTime);
        Executable H = () -> consumer.accept("H", waitTime);
        Executable I = () -> consumer.accept("I", waitTime);
        Executable J = () -> consumer.accept("J", waitTime);

        SerialeFlow full = new SerialeFlow();
        full.setWorkbench(workbench);

        ParalleFlow AB_P = new ParalleFlow();
        AB_P.setWorkbench(workbench);
        AB_P.push(A);
        AB_P.push(B);

        SerialeFlow ABC_S = new SerialeFlow();
        ABC_S.setWorkbench(workbench);
        ABC_S.push(AB_P);
        ABC_S.push(C);

        SerialeFlow FH_S = new SerialeFlow();
        FH_S.setWorkbench(workbench);
        FH_S.push(F);
        FH_S.push(H);

        ParalleFlow DE_P = new ParalleFlow();
        DE_P.setWorkbench(workbench);
        DE_P.push(D);
        DE_P.push(E);

        SerialeFlow DEG_S = new SerialeFlow();
        DEG_S.setWorkbench(workbench);
        DEG_S.push(DE_P);
        DEG_S.push(G);

        ParalleFlow DEG_FH_P = new ParalleFlow();
        DEG_FH_P.setWorkbench(workbench);
        DEG_FH_P.push(DEG_S);
        DEG_FH_P.push(FH_S);


        full.push(ABC_S);
        full.push(DEG_FH_P);
        full.push(I);
        full.push(J);

        // 开始工作～
        worker.work(full);


        SleepHelper.sleepSeconds(4000);
    }

    @Test
    public void testPool() {
        ExecutorService service = Executors.newFixedThreadPool(1);
        service.submit(() -> System.out.println("hello"));
        log.info("Thread Pool");
    }


}