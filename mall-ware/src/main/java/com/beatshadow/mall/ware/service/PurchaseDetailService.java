package com.beatshadow.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.beatshadow.common.utils.PageUtils;
import com.beatshadow.mall.ware.entity.PurchaseDetailEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author gnehcgnaw
 * @email gnehcgnaw@gmail.com
 * @date 2020-05-18 07:08:11
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<PurchaseDetailEntity> listDetailByPurchaseId(Long id);

}

