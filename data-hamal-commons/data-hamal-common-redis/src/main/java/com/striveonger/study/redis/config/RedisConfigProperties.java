package com.striveonger.study.redis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-06-29 11:25
 */
@Component
@ConfigurationProperties(prefix = "spring.redis")
public class RedisConfigProperties implements Serializable {

    /**
     * 后面需要什么配置, 再回来加呗, 参考:
     * 通用配置: {@link org.redisson.config.BaseConfig}
     * 单点模式: {@link org.redisson.config.SingleServerConfig}
     * 主从模式: {@link org.redisson.config.MasterSlaveServersConfig}
     * 哨兵模式: {@link org.redisson.config.SentinelServersConfig}
     * 集群模式: {@link org.redisson.config.ClusterServersConfig}
     */

    private static final long serialVersionUID = 8815222005846355408L;
    private String username;
    private String password;

    /**
     * 模式选择: single, maslaver, sentinel, cluster
     */
    private String model;
    private Single single;
    private Maslaver maslaver;
    private Sentinel sentinel;
    private Custer cluster;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Single getSingle() {
        return single;
    }

    public void setSingle(Single single) {
        this.single = single;
    }

    public Maslaver getMaslaver() {
        return maslaver;
    }

    public void setMaslaver(Maslaver maslaver) {
        this.maslaver = maslaver;
    }

    public Sentinel getSentinel() {
        return sentinel;
    }

    public void setSentinel(Sentinel sentinel) {
        this.sentinel = sentinel;
    }

    public Custer getCluster() {
        return cluster;
    }

    public void setCluster(Custer cluster) {
        this.cluster = cluster;
    }

    /**
     * 单节点模式
     */
    public static class Single {
        private String address;
        public int database = 0; // 默认0号数据库呗

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getDatabase() {
            return database;
        }

        public void setDatabase(int database) {
            this.database = database;
        }
    }

    /**
     * 主从模式
     */
    public static class Maslaver {
        // TODO: 主从模式
    }

    /**
     * 哨兵模式
     */
    public static class Sentinel {
        // TODO: 哨兵模式
    }

    /**
     * 集群模式
     */
    public static class Custer {
        // TODO 集群模式
        private List<String> addresses;

        public List<String> getAddresses() {
            return addresses;
        }

        public void setAddresses(List<String> addresses) {
            this.addresses = addresses;
        }
    }



}