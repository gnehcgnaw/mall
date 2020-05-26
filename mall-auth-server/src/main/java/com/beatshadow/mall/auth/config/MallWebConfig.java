package com.beatshadow.mall.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/25 18:39
 */
@Configuration
public class MallWebConfig implements WebMvcConfigurer {
    /**
     * 视图映射
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //登录也不映射，因为要写一些自己的逻辑
        //registry.addViewController("/login.html").setViewName("login");
        registry.addViewController("/reg.html").setViewName("reg");

    }
}
