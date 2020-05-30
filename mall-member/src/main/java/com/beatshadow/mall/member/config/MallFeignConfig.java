package com.beatshadow.mall.member.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * 解决feign调用  请求头丢失的问题       [单线程模式]
 *      异步请求会丢失线程的上下文信息  {@link OrderServiceImpl#confirmOrder()}    【 异步编排的导致feign的问题】
 *
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/28 14:19
 */
@Slf4j
@Configuration
public class MallFeignConfig {
    @Bean
    public RequestInterceptor requestInterceptor (){
        return  new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                log.debug("当前线程：{}" ,Thread.currentThread().getName());
                //获取当前的请求数据
                RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
                ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
                if (servletRequestAttributes!=null){
                    //判断是否为null是为了解决在不需要共享响应头的时候要保证session可用
                    // {@link OrderServiceImpl#confirmOrder()}    【 异步编排的导致feign的问题】
                    // org.springframework.web.util.NestedServletException: Request processing failed; nested exception is java.lang.IllegalStateException: Cannot create a session after the response has been committed
                    //	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1014) ~[spring-webmvc-5.2.5.RELEASE.jar:5.2.5.RELEASE]
                    //	at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:898) ~[spring-webmvc-5.2.5.RELEASE.jar:5.2.5.RELEASE]


                    //老请求 ,
                    HttpServletRequest request = servletRequestAttributes.getRequest();
                    if (request != null) {
                        //同步请求头中的数据，Cookie
                        String cookie = request.getHeader("Cookie");
                        template.header("Cookie",cookie);
                    }
                }
                log.debug("feign添加拦截器");
            }
        };
    }
}
