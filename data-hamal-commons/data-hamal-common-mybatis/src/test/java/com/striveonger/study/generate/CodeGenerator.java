package com.striveonger.study.generate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mr.Lee
 * @description:
 * @date 2022-10-30 15:02
 */
public class CodeGenerator {

    private final Logger log = LoggerFactory.getLogger(CodeGenerator.class);

    public static void main(String[] args) {
        // FastAutoGenerator.create(
        //                 "jdbc:mysql://192.168.10.222:3306/omm?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf-8&autoReconnect=true&serverTimezone=Asia/Shanghai",
        //                 "practice_user",
        //                 "user123")
        //         .globalConfig(builder -> {
        //             builder.author("Mr.Lee") // 设置作者
        //                     // .enableSwagger() // 开启 swagger 模式
        //                     // .fileOverride() // 覆盖已生成文件
        //                     .outputDir("/Users/striveonger/tmp"); // 指定输出目录
        //         })
        //         .packageConfig(builder -> {
        //             builder.parent("com.cecbrain.omm") // 设置父包名
        //                     .moduleName("alert.entity") // 设置父包模块名
        //                     .pathInfo(Collections.singletonMap(OutputFile.xml, "/Users/striveonger/tmp")); // 设置mapperXml生成路径
        //         })
        //         .strategyConfig(builder -> {
        //             // builder.addInclude("t_simple") // 设置需要生成的表名
        //             //        .addTablePrefix("t_", "c_"); // 设置过滤表前缀
        //             builder.addInclude("alert_message");
        //             builder.addInclude("alert_query");
        //             builder.addInclude("alert_record");
        //             builder.addInclude("alert_rule");
        //             builder.addInclude("silence");
        //         })
        //         .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
        //         .execute();
    }

}