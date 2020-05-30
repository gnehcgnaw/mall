package com.beatshadow.mall.seckill.service.impl;

import com.alibaba.fastjson.JSON;
import com.beatshadow.common.utils.R;
import com.beatshadow.mall.seckill.feign.CouponFeignService;
import com.beatshadow.mall.seckill.feign.ProductFeignService;
import com.beatshadow.mall.seckill.service.SeckillService;
import com.beatshadow.mall.seckill.to.SeckillSkuRedisTo;
import com.beatshadow.mall.seckill.vo.SeckillSessionsWithSkus;
import com.beatshadow.mall.seckill.vo.SeckillSkuVo;
import com.beatshadow.mall.seckill.vo.SkuInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/30 21:01
 */
@Slf4j
@Service
public class SeckillServiceImpl implements SeckillService {

    private final String SESSION_CACHE_PREFIX = "seckill:sessions:";

    private final String SKUKILL_CACHE_PREDIX = "seckill:skus";
    /**
     *    +商品随机码
     */
    private final String SKU_STOCK_SEMAPHORE="seckill:stock:";
    private CouponFeignService couponFeignService ;

    private StringRedisTemplate stringRedisTemplate ;

    private ProductFeignService productFeignService ;


    private RedissonClient redissonClient ;

    public SeckillServiceImpl(CouponFeignService couponFeignService, StringRedisTemplate stringRedisTemplate, ProductFeignService productFeignService, RedissonClient redissonClient) {
        this.couponFeignService = couponFeignService;
        this.stringRedisTemplate = stringRedisTemplate;
        this.productFeignService = productFeignService;
        this.redissonClient = redissonClient;
    }


    @Override
    public void uploadSeckillSkuLatest3Days() {
        //1、去扫描参与秒杀的活动【远程调用mall-coupon】
        R latest3DaySession = couponFeignService.getLatest3DaySession();
        if (latest3DaySession.getCode()==0){
            ArrayList<LinkedHashMap<String,Object>> seckillSessionEntities = (ArrayList<LinkedHashMap<String,Object>>)latest3DaySession.get("seckillSessionEntities");
            if (seckillSessionEntities!=null&&seckillSessionEntities.size()>0){
                String string = JSON.toJSONString(seckillSessionEntities);
                List<SeckillSessionsWithSkus> seckillSessionsWithSkusList = JSON.parseArray(string, SeckillSessionsWithSkus.class);
                //2、叫数据缓存到redis key = startTime+endTime  value = skuIds
                if (seckillSessionsWithSkusList!=null&&seckillSessionsWithSkusList.size()>0){
                    saveSessionInfo(seckillSessionsWithSkusList);

                    saveSessionSkuInfo(seckillSessionsWithSkusList);
                }

            }
        }
    }

    /**
     * 缓存活动信息
     * @param seckillSessionsWithSkusList
     */
    private void saveSessionInfo(List<SeckillSessionsWithSkus> seckillSessionsWithSkusList){
        seckillSessionsWithSkusList.stream().forEach(session ->{
            Long startTime = session.getStartTime().getTime();
            Long endTime = session.getEndTime().getTime();
            String key = SESSION_CACHE_PREFIX+startTime+"_"+endTime ;
            //幂等性
            if (!stringRedisTemplate.hasKey(key)){
                List<String> value = session.getRelationSkus().stream().map((item)->{
                    //场次+skuid 这么做是为了防止做幂等性引发的不同场次是有一个sku的问题
                    String promotionSessionId = item.getPromotionSessionId().toString();
                    String skuId  = item.getSkuId().toString();
                    return promotionSessionId+"_"+skuId ;
                }).collect(Collectors.toList());
                stringRedisTemplate.opsForList().leftPushAll(key,value);
                log.debug("saveSessionInfo,key={},value={}",key,value);
            }
        });

    }

    /**
     * 缓存活动的关联商品信息
     * @param seckillSessionsWithSkusList
     */
    private void saveSessionSkuInfo(List<SeckillSessionsWithSkus> seckillSessionsWithSkusList){
        seckillSessionsWithSkusList.stream().forEach(session ->{
            BoundHashOperations<String, Object, Object> stringObjectObjectBoundHashOperations = stringRedisTemplate.boundHashOps(SKUKILL_CACHE_PREDIX);
            List<SeckillSkuVo> relationSkus = session.getRelationSkus();
            if (relationSkus!=null&& relationSkus.size()>0){
                relationSkus.stream().forEach((seckillSkuVo)->{
                    //4.商品的随机码 防止黑客攻击   seckill?skuId=1&code=XXXX  【公平的秒杀】
                    String randomCode = UUID.randomUUID().toString().replace("-", "");
                    //幂等性控制
                    //这里不应该使用stringRedisTemplate.haskey了 【切记】找了半天才发现错误
                    if (!stringObjectObjectBoundHashOperations.hasKey(String.valueOf(seckillSkuVo.getPromotionSessionId())+"_"+String.valueOf(seckillSkuVo.getSkuId()))){
                        //1.shu的基本信息
                        SeckillSkuRedisTo seckillSkuRedisTo = new SeckillSkuRedisTo();
                        R info = productFeignService.info(seckillSkuVo.getSkuId());
                        if (info.getCode()==0){
                            LinkedHashMap<String,Object> skuInfo = (LinkedHashMap<String,Object>)info.get("skuInfo");
                            String string = JSON.toJSONString(skuInfo);
                            SkuInfoVo skuInfoVo = JSON.parseObject(string, SkuInfoVo.class);
                            seckillSkuRedisTo.setSkuInfo(skuInfoVo);
                        }
                        //2.sku的秒杀信息
                        BeanUtils.copyProperties(seckillSkuVo,seckillSkuRedisTo);
                        //3.设置秒杀的开始时间和结束时间
                        seckillSkuRedisTo.setStartTime(session.getStartTime().getTime());
                        seckillSkuRedisTo.setEndTime(session.getEndTime().getTime());

                        seckillSkuRedisTo.setRandomCode(randomCode);

                        String skuId = seckillSkuVo.getSkuId().toString();
                        Long promotionSessionId = seckillSkuVo.getPromotionSessionId();
                        String key = promotionSessionId+"_"+skuId ;
                        stringObjectObjectBoundHashOperations.put(key, JSON.toJSONString(seckillSkuRedisTo));
                        log.debug("saveSessionSkuInfo seckillSkuRedisTo ,key={},value={}",key,JSON.toJSONString(seckillSkuRedisTo));
                        //5.设置秒杀商品的分布式信号量，即库存的扣减信息【库存】【携带随机码减库存】
                        RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + randomCode);
                        //商品可以秒杀的数量
                        semaphore.trySetPermits(seckillSkuVo.getSeckillCount());
                        log.debug("saveSessionSkuInfo semaphore ,key={},value={}",SKU_STOCK_SEMAPHORE + randomCode,seckillSkuVo.getSeckillCount());

                    }

                });
            }
        } );
    }
}
