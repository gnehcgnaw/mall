package com.beatshadow.mall.product;

import com.beatshadow.mall.product.entity.BrandEntity;
import com.beatshadow.mall.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MallProductApplicationTests {

    @Autowired
    BrandService brandService ;

    @Test
    void contextLoads() {
        BrandEntity brandEntity = new BrandEntity() ;
        brandEntity.setName("华为");
        brandService.save(brandEntity);
    }

}
