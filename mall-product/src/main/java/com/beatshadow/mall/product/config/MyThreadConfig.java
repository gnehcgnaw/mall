package com.beatshadow.mall.product.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/25 16:08
 */
//@EnableConfigurationProperties(ThreadPoolExecutor.class)
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
