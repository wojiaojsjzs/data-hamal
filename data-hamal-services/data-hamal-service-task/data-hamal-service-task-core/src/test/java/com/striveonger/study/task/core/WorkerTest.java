package com.striveonger.study.task.core;


import cn.hutool.core.date.DateUtil;
import com.striveonger.study.task.core.flow.ParalleFlow;
import com.striveonger.study.task.core.flow.SerialFlow;
import com.striveonger.study.tools.SleepHelper;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

        int waitTime = 3;
        Executable A = () -> {
            System.out.println("A start " + DateUtil.now());
            SleepHelper.sleepSeconds(1);
            System.out.println("A end " + DateUtil.now());
        };
        Executable B = () -> {
            System.out.println("B start " + DateUtil.now());
            SleepHelper.sleepSeconds(2);
            System.out.println("B end " + DateUtil.now());
        };
        Executable C = () -> {
            SleepHelper.sleepSeconds(waitTime);
            System.out.println("C");
        };
        Executable D = () -> {
            SleepHelper.sleepSeconds(waitTime);
            System.out.println("D");
        };
        Executable E = () -> {
            SleepHelper.sleepSeconds(waitTime);
            System.out.println("E");
        };
        Executable F = () -> {
            SleepHelper.sleepSeconds(waitTime);
            System.out.println("F");
        };
        Executable G = () -> {
            SleepHelper.sleepSeconds(waitTime);
            System.out.println("G");
        };
        Executable H = () -> {
            SleepHelper.sleepSeconds(waitTime);
            System.out.println("H");
        };
        Executable I = () -> {
            SleepHelper.sleepSeconds(waitTime);
            System.out.println("I");
        };
        Executable J = () -> {
            SleepHelper.sleepSeconds(waitTime);
            System.out.println("J");
        };

        SerialFlow full = new SerialFlow();
        // A, B =>
        // SerialFlow flow_1 = new SerialFlow();
        ParalleFlow flow_1 = new ParalleFlow();
        flow_1.setWorkArea(workArea);
        flow_1.addTask(A);
        flow_1.addTask(B);

        ExecutorService service = Executors.newFixedThreadPool(1);
        service.submit(flow_1);

        SleepHelper.sleepSeconds(4);
    }

    @Test
    public void testPool() {
        ExecutorService service = Executors.newFixedThreadPool(1);
        service.submit(() -> System.out.println("hello"));

    }



}