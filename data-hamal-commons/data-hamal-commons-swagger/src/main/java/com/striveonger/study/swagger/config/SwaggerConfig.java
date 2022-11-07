package com.striveonger.study.swagger.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @author Mr.Lee
 * @description:
 * @date 2022-11-03 21:26
 */
@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfig {
    private final Logger log = LoggerFactory.getLogger(SwaggerConfig.class);

    @Bean(value = "dockerBean")
    public Docket dockerBean() {
        //指定使用Swagger2规范
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        //描述字段支持Markdown语法
                        .description("# Data Hamal RESTful APIs")
                        .version("0.0.1")
                        .build())
                .select()
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}