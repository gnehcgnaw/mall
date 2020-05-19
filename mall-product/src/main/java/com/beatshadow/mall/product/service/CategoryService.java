package com.beatshadow.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.beatshadow.common.utils.PageUtils;
import com.beatshadow.mall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author gnehcgnaw
 * @email gnehcgnaw@gmail.com
 * @date 2020-05-18 06:14:32
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> queryTree();

    void removeMenuByIds(List<Long> catIds);

    Long[] findCatelogPath(Long catelogId);
}

