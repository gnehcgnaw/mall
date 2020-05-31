package com.beatshadow.mall.seckill.controller;

import com.beatshadow.common.utils.R;
import com.beatshadow.mall.seckill.service.SeckillService;
import com.beatshadow.mall.seckill.to.SeckillSkuRedisTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/31 03:40
 */
@RestController
public class SeckillController {

    private SeckillService seckillService ;

    public SeckillController(SeckillService seckillService) {
        this.seckillService = seckillService;
    }

    /**
     * 当前时间要参与秒杀的商品
     * @return
     */
    @GetMapping("/getCurrentSeckillSkus")
    public R getCurrentSeckillSkus(){
        List<SeckillSkuRedisTo> seckillSkuRedisTos = seckillService.getCurrentSeckillSkus();
        return R.ok().put("seckillSkuRedisTos",seckillSkuRedisTos);
    }

    /**
     * 返回某个商品的秒杀预告
     */
    @GetMapping("/sku/seckill/{skuId}")
    public R getSkuSeckillInfo(@PathVariable("skuId") Long skuId){
        SeckillSkuRedisTo  seckillSkuRedisTo= seckillService.getSkuSeckillInfo(skuId);
        return R.ok().put("seckillSkuRedisTo",seckillSkuRedisTo);
    }

    /**
     * http://seckill.mall.com/kill?killId=5_27&key=b111f9e25992452181624ce776acc54d&num=1
     */
    @GetMapping("/kill")
    public R seckill(@RequestParam("killId") String killId , @RequestParam("key") String key ,@RequestParam("num")  Long num){
        //判断是否登录

        return R.ok();
    }
}
