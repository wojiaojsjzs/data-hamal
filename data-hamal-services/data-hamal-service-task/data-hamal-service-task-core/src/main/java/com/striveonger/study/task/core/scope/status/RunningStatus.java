package com.striveonger.study.task.core.scope.status;


import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import com.striveonger.study.task.core.constant.StepStatus;
import com.striveonger.study.task.core.constant.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;


/**
 * @author Mr.Lee
 * @description: 运行时状态(bitmap)
 * @date 2023-04-17 14:41
 */
public class RunningStatus {
    private final Logger log = LoggerFactory.getLogger(RunningStatus.class);

    private int total;

    private long[] states;

    private volatile boolean suspend = false;

    private volatile boolean aborted = false;

    public RunningStatus() { }

    public RunningStatus(int total) {
        this.total = total;
        this.states = new long[(total >> 5) + 1];
    }

    /**
     * 更新Step执行状态
     */
    public void update(int num, StepStatus status) {
        update(num << 1, status.getCode() & 1);
        update((num << 1) + 1, (status.getCode() >> 1) & 1);
    }

    private void update(int num, int x) {
        if (x == 1) {
            states[num >> 6] |= 1L << (num & 63);
        } else {
            states[num >> 6] &= ~(1L << (num & 63));
        }
    }

    /**
     * 步骤的执行状态
     */
    public StepStatus stepStatus(int num) {
        num <<= 1;
        int x = (states[num >> 6] & 1L << ((num + 1) & 63)) == 0 ? 0 : 1;
        x <<= 1;
        x |= (states[num >> 6] & 1L << (num & 63)) == 0 ? 0 : 1;
        return StepStatus.of(x);
    }

    /**
     * 任务的执行状态
     */
    public TaskStatus taskStatus() {
        int[] cnt = new int[4];
        for (int i = 0; i < total; i++) {
            // i >> 5 所在索引
            // ((i & 31) << 1) 要右移的位数
            int x = (int) ((states[i >> 5] >>> ((i & 31) << 1)) & 3);
            cnt[x]++;
        }
        if (this.aborted) return TaskStatus.ABORT;
        if (this.suspend) return TaskStatus.SUSPEND;
        if (cnt[0] == total) return TaskStatus.NONE;
        if (cnt[3] == total) return TaskStatus.COMPLETE;
        if (cnt[2] > 0) return TaskStatus.FAIL;
        if (cnt[1] > 0) return TaskStatus.RUNNING;
        // 咋地, 要上天啊~
        log.error("status count: {}, states: {}", Arrays.toString(cnt), Arrays.toString(states));
        throw new CustomException(ResultStatus.ACCIDENT);
    }

    /**
     * 暂停任务
     */
    @Deprecated
    public void pause() {
        throw new CustomException(ResultStatus.NON_SUPPORT, "Not support task pause...");
        // this.suspend = true;
    }

    /**
     * 中止任务
     */
    @Deprecated
    public void abort() {
        throw new CustomException(ResultStatus.NON_SUPPORT, "Not support task abort...");
        // this.aborted = true;
    }

}
