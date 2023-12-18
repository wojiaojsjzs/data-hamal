package com.striveonger.study.mybatis.config;

import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.audit.AuditManager;
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Mr.Lee
 * @description: MyBatis 配置文件
 * @date 2022-11-02 21:26
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
public class MybatisConfig implements MyBatisFlexCustomizer {
    private final Logger log = LoggerFactory.getLogger(MybatisConfig.class);

    // 没有解决无法读取父级配置文件的问题，临时用规则限制
    private final String basePackage = "com.striveonger.study.*.mapper";

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setBasePackage(basePackage);
        return configurer;
    }

    @Override
    public void customize(FlexGlobalConfig config) {
        //开启审计功能
        AuditManager.setAuditEnable(true);
        //设置 SQL 审计收集器
        AuditManager.setMessageCollector(
                message -> log.info("{}, {}ms", message.getFullSql(), message.getElapsedTime())
        );

        config.setLogicDeleteColumn("deleted");
        config.setNormalValueOfLogicDelete(0);
        config.setDeletedValueOfLogicDelete(1);
    }
}