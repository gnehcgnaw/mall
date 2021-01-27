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
        //注册两个订阅者
        for (int i = 0; i < 2; i++) {
            MqttUserRegisterVo mqttUserRegisterVo = new MqttUserRegisterVo();
            mqttUserRegisterVo.setUserName("sub_"+i);
            mqttUserRegisterVo.setPassword("sub_"+i);
            mqttUserService.register(mqttUserRegisterVo);
        }
        //注册两个发布者
        for (int j = 0; j < 2; j++) {
            MqttUserRegisterVo mqttUserRegisterVo = new MqttUserRegisterVo();
            mqttUserRegisterVo.setUserName("pub_"+j);
            mqttUserRegisterVo.setPassword("pub_"+j);
            mqttUserService.register(mqttUserRegisterVo);
        }
    }

    @Test
    void checkUsernameUnique() {
        mqttUserService.checkUsernameUnique("emqx");
    }
}