package com.beatshadow.mall.mqtt.service;

import com.beatshadow.mall.mqtt.entity.MqttAcl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2021/1/20 22:22
 */
@SpringBootTest
public class MqttAclServiceTest {
    @Autowired
    private MqttAclService mqttAclService ;

    @Test
    public void save(){
       //订阅者
        for (int i = 0; i < 2; i++) {
            mqttAclService.save(MqttAcl.builder()
                    .allow(1)
                    .username("sub_"+i)
                    .access(1)
                    .topic("/testtopic"+i)
                    .build());
        }
       //发布者
        for (int j = 0; j < 2; j++) {
            mqttAclService.save(MqttAcl.builder()
                    .allow(1)
                    .username("pub_"+j)
                    .access(2)
                    .topic("/testtopic"+j)
                    .build());
        }
    }
}