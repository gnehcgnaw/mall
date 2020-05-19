package com.beatshadow.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.beatshadow.common.utils.PageUtils;
import com.beatshadow.mall.product.entity.AttrGroupEntity;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author gnehcgnaw
 * @email gnehcgnaw@gmail.com
 * @date 2020-05-18 06:14:32
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params );
    PageUtils queryPage(Map<String, Object> params, Long catelogId);

    AttrGroupEntity selectById(Long attrGroupId);

    List<AttrGroupEntity> selectList(Long catelogId);
}

