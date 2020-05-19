package com.beatshadow.mall.product.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.beatshadow.common.utils.PageUtils;
import com.beatshadow.mall.product.entity.AttrAttrgroupRelationEntity;
import com.beatshadow.mall.product.vo.AttrGroupRelationVo;

import java.util.List;
import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author gnehcgnaw
 * @email gnehcgnaw@gmail.com
 * @date 2020-05-18 06:14:32
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    AttrAttrgroupRelationEntity queryByAttrId(Long attrId);

    Integer selectCount(Long attr_id);

    void insert(AttrAttrgroupRelationEntity relationEntity);

    List<AttrAttrgroupRelationEntity> selectList(Long attr_group_id);

    void deleteBatchRelation(List<AttrAttrgroupRelationEntity> entities);

    List<AttrAttrgroupRelationEntity> selectList(List<Long> collect);

    void saveBatch(List<AttrGroupRelationVo> vos);
}

