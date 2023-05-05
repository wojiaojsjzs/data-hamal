package com.striveonger.study.task.core;

import com.striveonger.study.task.core.constant.StepStatus;
import com.striveonger.study.task.core.scope.status.RuntimeStatus;

public class RunningStatusTest {

    public static void main(String[] args) {
        int total = 40;
        RuntimeStatus status = new RuntimeStatus(total);

        System.out.println("==========初始化=====");
        System.out.println("Task Status: " + status.taskStatus());

        System.out.println("==========开始执行=====");
        for (int i = 0; i < total; i++) {
            System.out.println("Step Status: " + status.stepStatus(i));
            status.update(i, StepStatus.RUNNING);
            System.out.println("Step Status: " + status.stepStatus(i));
            System.out.println("Task Status: " + status.taskStatus());
        }

        System.out.println("==========逐步执行完成=====");
        for (int i = 0; i < total; i++) {
            System.out.println("Step Status: " + status.stepStatus(i));
            if (i % 7 == 1) {
                status.update(i, StepStatus.FAIL);
            } else {
                status.update(i, StepStatus.COMPLETE);
            }
            System.out.println("Step Status: " + status.stepStatus(i));
            System.out.println("Task Status: " + status.taskStatus());
        }
    }

    public static void debug(long x) {
        System.out.println(x);
        StringBuilder s = new StringBuilder();
        for (int i = 63; i >= 0; i--) {
            s.append((x >> i) & 1);
            if (i % 4 == 0 && i > 0) {
                s.append("-");
            }
        }
        System.out.println(s);
    }
}