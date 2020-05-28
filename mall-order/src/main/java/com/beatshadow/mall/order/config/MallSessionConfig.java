package com.beatshadow.mall.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * https://github.com/spring-projects/spring-session/
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/26 18:59
 */
@Configuration
public class MallSessionConfig {
    /**
     *  https://docs.spring.io/spring-session/docs/2.3.0.RELEASE/reference/html5/#api-cookieserializer
     */
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        cookieSerializer.setCookieName("MALLSESSION");
        cookieSerializer.setCookiePath("/");
        //提高session级别
        cookieSerializer.setDomainName("mall.com");
        //cookieSerializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");
        return cookieSerializer;
    }

    /**
     * https://docs.spring.io/spring-session/docs/2.3.0.RELEASE/reference/html5/#api-redisindexedsessionrepository-config
     * @return
     */
/*    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer(){
       //使用GenericJackson2JsonRedisSerializer序列化失败【要求VO必须在同一个路径】
        return new GenericFastJsonRedisSerializer();
    }*/

}
