package com.beatshadow.mall.product.service.impl;

import com.beatshadow.mall.product.vo.SkuItemSaleAttrVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beatshadow.common.utils.PageUtils;
import com.beatshadow.common.utils.Query;

import com.beatshadow.mall.product.dao.SkuSaleAttrValueDao;
import com.beatshadow.mall.product.entity.SkuSaleAttrValueEntity;
import com.beatshadow.mall.product.service.SkuSaleAttrValueService;


/**
 * @author gnehcgnaw
 */
@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuSaleAttrValueEntity> page = this.page(
                new Query<SkuSaleAttrValueEntity>().getPage(params),
                new QueryWrapper<SkuSaleAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuItemSaleAttrVo> getSaleAttrBySpuId(Long spuId) {
        List<SkuItemSaleAttrVo>  skuItemSaleAttrVos = this.baseMapper.getSaleAttrBySpuId(spuId);
        return skuItemSaleAttrVos;
    }

    @Override
    public List<String> getSkuSaleAttrValues(Long skuId) {
        return this.baseMapper.getSkuSaleAttrValues(skuId);
    }

}