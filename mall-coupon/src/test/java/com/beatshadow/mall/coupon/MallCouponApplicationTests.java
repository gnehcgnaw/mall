package com.beatshadow.mall.coupon;

import com.beatshadow.mall.coupon.entity.CouponEntity;
import com.beatshadow.mall.coupon.service.CouponService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
class MallCouponApplicationTests {

    @Autowired
    CouponService couponService ;

    @Test
    void contextLoads() {
        List<CouponEntity> list = couponService.list();
        log.debug("list is {}",list);
    }

}
