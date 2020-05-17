package com.beatshadow.mall.product.dao;

import com.beatshadow.mall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author gnehcgnaw
 * @email gnehcgnaw@gmail.com
 * @date 2020-05-18 06:14:32
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
