package com.atguigu.gmall.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class GmallSwagger2Config {

    @Bean("后台用户模块")
    public Docket userApis() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("后台用户模块")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.atguigu.gmall.admin.ums.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .enable(true);
    }

    @Bean("后台商品模块")
    public Docket productApis() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("后台商品模块")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.atguigu.gmall.admin.pms.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .enable(true);
    }

    @Bean("商品优选模块")
    public Docket cmsApis() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("商品优选模块")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.atguigu.gmall.admin.cms.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .enable(true);
    }

    @Bean("优惠产品模块")
    public Docket smsApis() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("优惠产品模块")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.atguigu.gmall.admin.sms.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .enable(true);
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("谷粒商城-后台管理系统平台接口文档")
                .description("提供pms、oms、ums、cms、sms模块的文档")
                .termsOfServiceUrl("http://www.atguigu.com/")
                .version("1.0")
                .build();
    }
}
