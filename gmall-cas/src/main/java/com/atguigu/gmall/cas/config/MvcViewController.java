package com.atguigu.gmall.cas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ：mei
 * @date ：Created in 2019/4/1 0001 下午 23:11
 * @description：
 * @modified By：
 * @version: $
 */
@Configuration
public class MvcViewController implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //registry.addViewController("/login.html").setViewName("login");
    }
}
