package com.beatshadow.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.beatshadow.common.utils.PageUtils;
import com.beatshadow.mall.ware.entity.WareSkuEntity;

import java.util.Map;

/**
 * 商品库存
 *
 * @author gnehcgnaw
 * @email gnehcgnaw@gmail.com
 * @date 2020-05-18 07:08:11
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);
}

