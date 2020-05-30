package com.beatshadow.mall.seckill.scheduled;

import com.beatshadow.mall.seckill.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 秒杀商品的定时上架
 *      每天晚上3点，上架最近三天需要秒杀的商品
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/30 20:54
 */
@Slf4j
@Service
public class SeckillSkuSchedule {


    private SeckillService seckillService ;

    private RedissonClient redissonClient ;

    /**
     * 秒杀商品上架的分布式锁
     */
    private final String upload_lock = "seckill:upload:lock";

    public SeckillSkuSchedule(SeckillService seckillService, RedissonClient redissonClient) {
        this.seckillService = seckillService;
        this.redissonClient = redissonClient;
    }


    /**
     * 接口要做幂等性
     *  上架过的就不需要重复上架了
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void uploadSeckillSkuLatest3Days(){
        RLock rLock = redissonClient.getLock(upload_lock);
        rLock.lock(15, TimeUnit.SECONDS);
        //1、重复上架无需处理
        try {
            seckillService.uploadSeckillSkuLatest3Days();
        } finally {
            rLock.unlock();
        }
    }
}
