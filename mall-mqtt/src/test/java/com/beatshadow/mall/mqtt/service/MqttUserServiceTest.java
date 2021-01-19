package com.beatshadow.mall.mqtt.service;

import com.beatshadow.mall.mqtt.vo.MqttUserRegisterVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2021/1/19 17:11
 */
@SpringBootTest
public class MqttUserServiceTest {
    @Autowired
    private MqttUserService mqttUserService ;

    @Test
    void register() {
        MqttUserRegisterVo mqttUserRegisterVo = new MqttUserRegisterVo();
        mqttUserRegisterVo.setUserName("test");
        mqttUserRegisterVo.setPassword("test");
        mqttUserService.register(mqttUserRegisterVo);

    }

    @Test
    void checkUsernameUnique() {
        mqttUserService.checkUsernameUnique("emqx");
    }
}