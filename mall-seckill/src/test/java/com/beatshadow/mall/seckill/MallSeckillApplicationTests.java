package com.beatshadow.mall.seckill;

import com.beatshadow.mall.seckill.scheduled.SeckillSkuSchedule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MallSeckillApplicationTests {

    @Autowired
    private SeckillSkuSchedule seckillSkuSchedule ;


    @Test
    void contextLoads() {
        seckillSkuSchedule.uploadSeckillSkuLatest3Days();
    }

}
