package com.beatshadow.mall.product;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/22 14:35
 */

@Slf4j
@SpringBootTest
public class RedisTests {

    @Autowired
    private StringRedisTemplate stringRedisTemplate ;

    @Test
    public void test(){
        String uuid = UUID.randomUUID().toString();
        String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else  return 0 end";
        // 1, 删除成功，2， 删除失败 【但是值就没必要接收了】(脚本返回的是null)
        //Long execute = stringRedisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList("lock"), uuid);
        //log.debug("{},释放锁{}",Thread.currentThread().getName(),execute);
        System.out.println(uuid);
        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent("lock", uuid, 5, TimeUnit.SECONDS);

        System.out.println(lock);
        if (lock){

            try {
                log.debug("执行程序");
                TimeUnit.SECONDS.sleep(2);
                log.debug("执行完成");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                Long execute = stringRedisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList("lock"), uuid);
                log.debug("解锁状况：{}" , execute);
            }


        }
    }
}
