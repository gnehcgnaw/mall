package com.beatshadow.mall.product;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.Semaphore;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/22 13:29
 */
@Slf4j
@SpringBootTest
public class RedissonTests {

    @Autowired
    private RedissonClient redissonClient ;

    /**
     * 获取RedissonClient
     */
    @Test
    public void getRedissonClient(){
        if (redissonClient != null) {
            System.out.println(redissonClient);
        }
    }
    //分布式锁和同步器
    //https://github.com/redisson/redisson/wiki/8.-%E5%88%86%E5%B8%83%E5%BC%8F%E9%94%81%E5%92%8C%E5%90%8C%E6%AD%A5%E5%99%A8
    //基于Redis的Redisson分布式可重入锁RLock Java对象实现了java.util.concurrent.locks.Lock接口。
    // 同时还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口。


}
