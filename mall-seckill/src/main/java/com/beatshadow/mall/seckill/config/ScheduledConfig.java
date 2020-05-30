package com.beatshadow.mall.seckill.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/30 20:55
 */
@EnableAsync
@EnableScheduling
@Configuration
public class ScheduledConfig {
    /**
     * 定时任务默认是阻塞的
     * TaskSchedulingProperties 的默认线程是1
     *
     * TaskExecutionProperties 异步任务，默认线程数为8
     * 如何异步执行线程池？
     * 1、在定时任务中使用异步编排
     * 2、配置线程数，但是这个有时候不好使
     * 3、让定时任务异步执行
     *      a、@EnableAsync
     *      b、@Async
     *
     */
}
