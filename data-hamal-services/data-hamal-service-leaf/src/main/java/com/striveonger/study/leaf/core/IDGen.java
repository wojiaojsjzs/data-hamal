package com.striveonger.study.leaf.core;

import cn.hutool.core.util.StrUtil;
import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import com.striveonger.study.leaf.core.common.ID;
import com.striveonger.study.leaf.core.segment.SegmentIDGen;
import com.striveonger.study.leaf.core.snowflake.SnowflakeIDGen;
import com.striveonger.study.leaf.service.ILeafAllocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface IDGen {

    Logger log = LoggerFactory.getLogger(IDGen.class);

    ID get(String key);

    boolean init();

    static Builder builder() {
        return new Builder();
    }

    class Builder {

        // 通用属性
        private String type;

        // Segment 模式
        private ILeafAllocService service;

        // Snowflake 模式
        // 节点名
        private String nodeName;
        // Zookeeper 地址
        private String zookeeperAddress;
        // Zookeeper 地址
        private Integer zookeeperPort;

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder service(ILeafAllocService service) {
            this.service = service;
            return this;
        }

        public Builder nodeName(String nodeName) {
            this.nodeName = nodeName;
            return this;
        }

        public Builder zookeeperAddress(String zookeeperAddress) {
            this.zookeeperAddress = zookeeperAddress;
            return this;
        }

        public Builder zookeeperPort(Integer zookeeperPort) {
            this.zookeeperPort = zookeeperPort;
            return this;
        }

        public IDGen build() {
            IDGen gen;
            if ("segment".equals(type)) {
                SegmentIDGen segment = new SegmentIDGen();
                segment.setDao(service);
                gen = segment;
            } else if ("snowflake".equals(type)) {
                // 检查初始化参数
                if (zookeeperPort <= 0 || StrUtil.isBlank(nodeName) || StrUtil.isBlank(zookeeperAddress)) {
                    log.error("构建参数出错...");
                    throw new CustomException(ResultStatus.ACCIDENT, "Snowflake Initialization Params Error...");
                }
                gen = new SnowflakeIDGen(zookeeperAddress, zookeeperPort, nodeName);
            } else {
                throw new CustomException("There is a problem with startup mode...");
            }
            if (gen.init()) {
                return gen;
            } else {
                throw new CustomException(String.format("%s initialization fail...", type));
            }
        }
    }

    // interface Builder {
    //     IDGen build();
    // }

    // static Builder builder() {
    //     Boolean segmentEnabled = SpringContextHolder.getProperties(LEAF_SEGMENT_ENABLED, false);
    //     Boolean snowflakeEnabled = SpringContextHolder.getProperties(LEAF_SNOWFLAKE_ENABLED, false);
    //     if (segmentEnabled && snowflakeEnabled) {
    //         // 不可以同时启动两种模式哦～
    //         log.error("两种模式不可以同时开启哦～");
    //         throw new CustomException("Mode repeatedly enabled...");
    //     } else if (segmentEnabled) {
    //         // 数据库号段模式, 强依赖数据库的稳定性
    //         log.info("开启数据库号段模式");
    //         return new SegmentBuilder();
    //     } else if (snowflakeEnabled) {
    //         // 雪花算法模式，依赖zookeeper
    //         log.info("开启雪花算法模式");
    //         return new SnowflakeBuilder();
    //     }
    //     log.error("没有选择开启的模式");
    //     throw new CustomException("Mode none enabled...");
    // }

    // class SegmentBuilder implements Builder {
    //     public IDGen build() {
    //         ILeafAllocService service = SpringContextHolder.getBean(ILeafAllocService.class);
    //         SegmentIDGen segment = new SegmentIDGen();
    //         segment.setDao(service);
    //         if (segment.init()) {
    //             return segment;
    //         } else {
    //             throw new CustomException("Segment Initialization Fail...");
    //         }
    //     }
    // }

    // class SnowflakeBuilder implements Builder {
    //     public IDGen build() {
    //         String nodeName = SpringContextHolder.getProperties(LEAF_NODE_NAME, "");
    //         String snowflakeAddress = SpringContextHolder.getProperties(LEAF_SNOWFLAKE_ADDRESS, "");
    //         Integer snowflakePort = SpringContextHolder.getProperties(LEAF_SNOWFLAKE_PORT, -1);
    //         // 检查参数～
    //         if (snowflakePort <= 0 || StrUtil.isBlank(nodeName) || StrUtil.isBlank(snowflakeAddress)) {
    //             log.error("构建参数出错...");
    //             throw new CustomException(ResultStatus.ACCIDENT);
    //         }
    //         SnowflakeIDGen snowflake = new SnowflakeIDGen(snowflakeAddress, snowflakePort, nodeName);
    //         if (snowflake.init()) {
    //             return snowflake;
    //         } else {
    //             throw new CustomException("Snowflake Initialization Fail...");
    //         }
    //     }
    // }

    // class DefaultBuilder implements Builder {
    //     @Override
    //     public IDGen build() {
    //         return new ZeroIDGen();
    //     }
    // }

}
