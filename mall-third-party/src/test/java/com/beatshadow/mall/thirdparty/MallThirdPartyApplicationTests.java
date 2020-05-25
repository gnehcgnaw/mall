package com.beatshadow.mall.thirdparty;

import com.beatshadow.mall.thirdparty.component.SmsComponent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MallThirdPartyApplicationTests {
    @Autowired
    private SmsComponent smsComponent ;

    @Test
    void contextLoads() {
    }

    @Test
    public void testSmsCode(){
        smsComponent.sendSmsCode("15104455502","123456");
    }

}
