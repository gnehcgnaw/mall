package com.beatshadow.mall.product.service.impl;

import com.beatshadow.mall.product.vo.AttrGroupRelationVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public List<AttrAttrgroupRelationEntity> selectList(Long attr_group_id) {
        return baseMapper.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attr_group_id));
    }

    @Override
    public void deleteBatchRelation(List<AttrAttrgroupRelationEntity> entities) {
        baseMapper.deleteBatchRelation(entities);
    }

    @Override
    public List<AttrAttrgroupRelationEntity> selectList(List<Long> collect) {
        return baseMapper.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", collect));
    }

    @Override
    public void saveBatch(List<AttrGroupRelationVo> vos) {
        List<AttrAttrgroupRelationEntity> collect = vos.stream().map(item -> {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());
        this.saveBatch(collect);
    }
}