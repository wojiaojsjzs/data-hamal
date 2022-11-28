package com.striveonger.study.leaf.constants;

/**
 * @author Mr.Lee
 * @description: 配置Key常量
 * @date 2022-11-27 08:37
 */
public class PropertiesConstants {

    /**
     * 服务的节点名
     */
    public static final String LEAF_NODE_NAME = "${data.hamal.leaf.name}";

    /**
     * 启动模式: 数据库号段模式是否开启 or 雪花算法模式是否开启(segment, snowflake)
     * TODO: 这里要留一个坑，一会儿学习SpringBoot动态配置时，需要补上。就是当开启号段模式时，才会加载MyBatis相关的内容。
     */
    public static final String LEAF_TYPE = "${data.hamal.leaf.type}";

    /**
     * 雪花算法模式下需要连接的Zookeeper地址
     */
    public static final String LEAF_ZOOKEEPER_ADDRESS = "${data.hamal.leaf.zookeeper.address}";

    /**
     * Leaf启动占用的端口号
     */
    public static final String LEAF_PORT = "${server.port}";

}