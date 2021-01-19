package com.beatshadow.mall.mqtt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.beatshadow.mall.mqtt.entity.MqttUser;
import com.beatshadow.mall.mqtt.vo.MqttUserRegisterVo;

/**
 * 
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2021/1/19 16:09
 */
public interface MqttUserService extends IService<MqttUser> {

    void register(MqttUserRegisterVo mqttUserRegisterVo);

    void checkUsernameUnique(String username);
}
