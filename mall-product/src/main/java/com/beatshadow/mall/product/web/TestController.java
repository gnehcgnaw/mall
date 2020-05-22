package com.beatshadow.mall.product.web;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/22 23:50
 */
@Slf4j
@RequestMapping("/test")
@RestController
public class TestController {

    private RedissonClient redissonClient ;

    public TestController(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * 停车   请求一次少一次许可
     * @return
     */
    @RequestMapping("/park")
    public String park(@PathVariable("permits") int permits ){
        RSemaphore semaphore = redissonClient.getSemaphore("semaphore");
        try {
            //拿许可
            semaphore.acquire(permits);

           // semaphore.tryAcquire(); 加时间可以做限流
            return "park "+ permits+"is success " ;

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null ;
    }

    /**
     * 开走  请求一次，多一个许可
     * @return
     */
    @RequestMapping("/execute-task")
    public String go(){
        RSemaphore semaphore = redissonClient.getSemaphore("semaphore");
        semaphore.release();
        return "release" ;
    }


    /**
     * 人都走了才能锁门
     * @return
     */
    //闭锁
    @RequestMapping("/lock-door")
    public String lockDoor (){
        RCountDownLatch countDownLatch = redissonClient.getCountDownLatch("countDownLatch");
        try {
            countDownLatch.trySetCount(5);
            countDownLatch.await();
            return "人都走了可以锁门了";
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null ;
    }

    /**
     * 走人
     * @return
     */
    @RequestMapping("/go-go-go")
    public String gogogo (){
        RCountDownLatch countDownLatch = redissonClient.getCountDownLatch("countDownLatch");
            log.debug("还有{}没走,我要走了！！！",countDownLatch.getCount());
            countDownLatch.countDown();
            return String.valueOf(countDownLatch.getCount()+1);
    }
}
