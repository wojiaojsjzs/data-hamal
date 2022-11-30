package com.striveonger.study.task.core;


import cn.hutool.core.date.DateUtil;
import com.striveonger.study.task.core.flow.ParalleFlow;
import com.striveonger.study.task.core.flow.SerialFlow;
import com.striveonger.study.tools.SleepHelper;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * @author Mr.Lee
 * @description: 工作区的测试类
 * @date 2022-11-30 11:01
 */
public class WorkerTest {

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
        WorkArea workArea = new WorkArea(1L);
        WorkArea.Worker worker = workArea.getWorker();

        Consumer<String> consumer = new Consumer<String>() {
            int waitTime = 1;
            @Override
            public void accept(String s) {
                System.out.println(s + " start " + DateUtil.now());
                SleepHelper.sleepSeconds(waitTime);
                System.out.println(s + " end " + DateUtil.now());
            }
        };

        Executable A = () -> consumer.accept("A");
        Executable B = () -> consumer.accept("B");
        Executable C = () -> consumer.accept("C");
        Executable D = () -> consumer.accept("D");
        Executable E = () -> consumer.accept("E");
        Executable F = () -> consumer.accept("F");
        Executable G = () -> consumer.accept("G");
        Executable H = () -> consumer.accept("H");
        Executable I = () -> consumer.accept("I");
        Executable J = () -> consumer.accept("J");

        SerialFlow full = new SerialFlow();
        full.setWorkArea(workArea);

        ParalleFlow AB_P = new ParalleFlow();
        AB_P.setWorkArea(workArea);
        AB_P.addTask(A);
        AB_P.addTask(B);

        SerialFlow ABC_S = new SerialFlow();
        ABC_S.setWorkArea(workArea);
        ABC_S.addTask(AB_P);
        ABC_S.addTask(C);

        SerialFlow FH_S = new SerialFlow();
        FH_S.setWorkArea(workArea);
        FH_S.addTask(F);
        FH_S.addTask(H);

        ParalleFlow DE_P = new ParalleFlow();
        DE_P.setWorkArea(workArea);
        DE_P.addTask(D);
        DE_P.addTask(E);

        SerialFlow DEG_S = new SerialFlow();
        DEG_S.setWorkArea(workArea);
        DEG_S.addTask(DE_P);
        DEG_S.addTask(G);

        ParalleFlow DEG_FH_P = new ParalleFlow();
        DEG_FH_P.setWorkArea(workArea);
        DEG_FH_P.addTask(DEG_S);
        DEG_FH_P.addTask(FH_S);


        full.addTask(ABC_S);
        full.addTask(DEG_FH_P);
        full.addTask(I);
        full.addTask(J);

        ExecutorService service = Executors.newFixedThreadPool(1);
        service.submit(full);

        SleepHelper.sleepSeconds(4000);
    }

    @Test
    public void testPool() {
        ExecutorService service = Executors.newFixedThreadPool(1);
        service.submit(() -> System.out.println("hello"));

    }



}