package com.beatshadow.mall.coupon.dao;

import com.beatshadow.mall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author gnehcgnaw
 * @email gnehcgnaw@gmail.com
 * @date 2020-05-18 06:50:28
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
