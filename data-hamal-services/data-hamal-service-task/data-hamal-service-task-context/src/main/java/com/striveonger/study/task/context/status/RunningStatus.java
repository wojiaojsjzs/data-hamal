package com.striveonger.study.task.context.status;


import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import com.striveonger.study.task.common.constant.StepStatus;
import com.striveonger.study.task.common.constant.TaskStatus;

/**
 * @author Mr.Lee
 * @description: 运行时状态(bitmap)
 * @date 2023-04-17 14:41
 */
public class RunningStatus {

    private int total;

    private long[] states;

    private volatile boolean suspend = false;

    public RunningStatus() {
    }

    public RunningStatus(int total) {
        this.total = total;
        this.states = new long[(total >> 5) + 1];
    }

    /**
     * @param num
     * @param status
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
     *
     * @param num
     * @return
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
     *
     * @return
     */
    public TaskStatus taskStatus() {
        int[] cnt = new int[4];
        for (int i = 0; i < total; i++) {
            // i >> 5 所在索引
            // ((i & 31) << 1) 要右移的位数
            int x = (int) ((states[i >> 5] >>> ((i & 31) << 1)) & 3);
            cnt[x]++;
        }
        if (this.suspend) return TaskStatus.SUSPEND;
        if (cnt[0] == total) return TaskStatus.NONE;
        if (cnt[3] == total) return TaskStatus.COMPLETE;
        if (cnt[2] > 0) return TaskStatus.FAIL;
        if (cnt[1] > 0) return TaskStatus.RUNNING;
        // 咋地, 要上天啊~
        throw new CustomException(ResultStatus.ACCIDENT);
    }

    public void pause() {
        this.suspend = true;
    }

}
