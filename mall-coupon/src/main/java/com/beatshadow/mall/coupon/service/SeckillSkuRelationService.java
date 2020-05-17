package com.beatshadow.mall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.beatshadow.common.utils.PageUtils;
import com.beatshadow.mall.coupon.entity.SeckillSkuRelationEntity;

import java.util.Map;

/**
 * 秒杀活动商品关联
 *
 * @author gnehcgnaw
 * @email gnehcgnaw@gmail.com
 * @date 2020-05-18 06:50:28
 */
public interface SeckillSkuRelationService extends IService<SeckillSkuRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

