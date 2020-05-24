package com.beatshadow.mall.product;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.beatshadow.mall.product.dao.AttrGroupDao;
import com.beatshadow.mall.product.dao.SkuSaleAttrValueDao;
import com.beatshadow.mall.product.entity.BrandEntity;
import com.beatshadow.mall.product.service.BrandService;
import com.beatshadow.mall.product.service.CategoryService;
import com.beatshadow.mall.product.vo.SkuItemSaleAttrVo;
import com.beatshadow.mall.product.vo.SkuItemVo;
import com.beatshadow.mall.product.vo.SpuItemAttrGroupVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@Slf4j
@SpringBootTest
class MallProductApplicationTests {

    @Autowired
    BrandService brandService;


    @Autowired
    CategoryService categoryService;

    @Autowired
    AttrGroupDao attrGroupDao ;

    @Autowired
    SkuSaleAttrValueDao skuSaleAttrValueDao ;
    @Test
    public void testGetAttrGroupWithAttrsBySpuId(){
        List<SpuItemAttrGroupVo> attrGroupWithAttrsBySpuId = attrGroupDao.getAttrGroupWithAttrsBySpuId(13L,225L);
        log.info("attrGroupWithAttrsBySpuId：{}",attrGroupWithAttrsBySpuId);
    }

    @Test
    public void testGetSaleAttrBySpuId(){
        List<SkuItemSaleAttrVo> saleAttrBySpuId = skuSaleAttrValueDao.getSaleAttrBySpuId(13L);
        log.info("saleAttrBySpuId：{}",saleAttrBySpuId);
    }

    @Test
    public void testFindPath(){
        Long[] catelogPath = categoryService.findCatelogPath(225L);
        log.info("完整路径：{}", Arrays.asList(catelogPath));
    }


    @Test
    public void contextLoads() {

//        BrandEntity brandEntity = new BrandEntity();
//        brandEntity.setBrandId(1L);
//        brandEntity.setDescript("华为");

//
//        brandEntity.setName("华为");
//        brandService.save(brandEntity);
//        System.out.println("保存成功....");

//        brandService.updateById(brandEntity);


        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1L));
        list.forEach((item) -> {
            System.out.println(item);
        });

    }

}
