package com.beatshadow.mall.product.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beatshadow.common.utils.PageUtils;
import com.beatshadow.common.utils.Query;

import com.beatshadow.mall.product.dao.AttrAttrgroupRelationDao;
import com.beatshadow.mall.product.entity.AttrAttrgroupRelationEntity;
import com.beatshadow.mall.product.service.AttrAttrgroupRelationService;


@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrAttrgroupRelationEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public AttrAttrgroupRelationEntity queryByAttrId(Long attrId){
        return baseMapper.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
    }

    @Override
    public Integer selectCount(Long attr_id) {
        return  baseMapper.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr_id));
    }

    @Override
    public void insert(AttrAttrgroupRelationEntity relationEntity) {
        baseMapper.insert(relationEntity);
    }
}