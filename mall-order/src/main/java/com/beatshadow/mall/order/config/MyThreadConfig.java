package com.beatshadow.mall.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/25 16:08
 */
@Configuration
public class MyThreadConfig {
    @Bean
    public ThreadPoolExecutor threadPoolExecutor (ThreadPoolConfigProperties threadPoolConfigProperties){
         ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                 threadPoolConfigProperties.getCoreSize(),
                 threadPoolConfigProperties.getMaxSize(),
                 threadPoolConfigProperties.getKeepAliveTime(),
                 TimeUnit.SECONDS, new LinkedBlockingDeque<>(100000),
                 Executors.defaultThreadFactory(),
                 new ThreadPoolExecutor.AbortPolicy());
        return threadPoolExecutor ;
    }
}
