package com.striveonger.study.leaf.core.snowflake;


import com.google.common.base.Preconditions;
import com.striveonger.study.leaf.core.IDGen;
import com.striveonger.study.leaf.core.common.ID;
import com.striveonger.study.leaf.constants.Status;
import com.striveonger.study.leaf.core.common.IPUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * 雪花算法生成ID
 */
public class SnowflakeIDGen implements IDGen {

    private static final Logger log = LoggerFactory.getLogger(SnowflakeIDGen.class);

    private final long twepoch;
    private final long workerIdBits = 10L;
    private final long maxWorkerId = ~(-1L << workerIdBits); // 最大能够分配的workerid =1023
    private final long sequenceBits = 12L;
    private final long workerIdShift = sequenceBits;
    private final long timestampLeftShift = sequenceBits + workerIdBits;
    private final long sequenceMask = ~(-1L << sequenceBits);
    private long workerId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;
    private static final Random RANDOM = new Random();

    public SnowflakeIDGen(String zkAddress, int port, String leafName) {
        // Thu Nov 04 2010 09:42:54 GMT+0800 (中国标准时间)
        this(zkAddress, port, 1288834974657L, leafName);
    }

    /**
     * @param zookeeperAddress zk地址
     * @param port             snowflake监听端口
     * @param twepoch          起始的时间戳
     * @param leafName         节点名字
     *
     */
    public SnowflakeIDGen(String zookeeperAddress, int port, long twepoch, String leafName) {
        this.twepoch = twepoch;
        Preconditions.checkArgument(timeGen() > twepoch, "Snowflake not support twepoch gt currentTime");
        final String ip = IPUtils.getIp();
        SnowflakeZookeeperHolder holder = new SnowflakeZookeeperHolder(ip, String.valueOf(port), zookeeperAddress, leafName);
        log.info("twepoch:{} ,ip:{} ,zookeeperAddress:{} port:{}", twepoch, ip, zookeeperAddress, port);
        boolean initFlag = holder.init();
        if (initFlag) {
            workerId = holder.getWorkerID();
            log.info("START SUCCESS USE ZK WORKERID-{}", workerId);
        } else {
            Preconditions.checkArgument(initFlag, "Snowflake Id Gen is not init ok");
        }
        Preconditions.checkArgument(workerId >= 0 && workerId <= maxWorkerId, "workerID must gte 0 and lte 1023");
    }

    @Override
    public boolean init() {
        return true;
    }

    @Override
    public synchronized ID get(String key) {
        long timestamp = timeGen();
        // 防止系统时间回调, 导致ID冲突的情况
        if (timestamp < lastTimestamp) {
            long offset = lastTimestamp - timestamp;
            // 容忍系统回调上限为5毫秒
            if (offset <= 5) {
                try {
                    // 等待
                    wait(offset << 1);
                    timestamp = timeGen();
                    if (timestamp < lastTimestamp) {
                        return new ID(-1, Status.EXCEPTION);
                    }
                } catch (InterruptedException e) {
                    log.error("wait interrupted");
                    return new ID(-2, Status.EXCEPTION);
                }
            } else {
                return new ID(-3, Status.EXCEPTION);
            }
        }
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                //seq 为0的时候表示是下一毫秒时间开始对seq做随机
                sequence = RANDOM.nextInt(100);
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            //如果是新的ms开始
            sequence = RANDOM.nextInt(100);
        }
        lastTimestamp = timestamp;
        long id = ((timestamp - twepoch) << timestampLeftShift) | (workerId << workerIdShift) | sequence;
        return new ID(id, Status.SUCCESS);
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 获取系统时间戳
     * @return
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }

    public long getWorkerId() {
        return workerId;
    }

}
