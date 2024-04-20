package com.xiangjiahui.weblog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
@Profile("dev")//只在dev环境开启生效
public class Knife4jConfig {

    @Bean("webApi")
    public Docket createApiDoc(){
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(buildApiInfo())
                .groupName("Web 前台接口")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.xiangjiahui.weblog.web.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    /**
     * 构建Api文档信息
     * @return
     */
    public ApiInfo buildApiInfo(){
        return new ApiInfoBuilder()
                .title("Weblog 博客前台接口文档")//标题
                .description("Weblog 是一款由 Spring Boot + Vue 3.2 开发的前后端分离博客。")
                .termsOfServiceUrl("https://www.xiangjiahui.com/")//API 服务条款
                .contact(new Contact("xiangjiahui", "https://www.xiangjiahui.com/", "2464674651@qq.com"))
                .version("1.0")//版本号
                .build();
    }
}
