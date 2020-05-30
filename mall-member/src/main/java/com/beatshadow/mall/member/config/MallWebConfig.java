package com.beatshadow.mall.member.config;

import com.beatshadow.mall.member.interceptor.LoginUserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/27 15:11
 */
@Configuration
public class MallWebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //放行/member/**
        registry.addInterceptor(new LoginUserInterceptor()).addPathPatterns("/**").excludePathPatterns("/member/member/login","/member/memberreceiveaddress/**");
    }
}
