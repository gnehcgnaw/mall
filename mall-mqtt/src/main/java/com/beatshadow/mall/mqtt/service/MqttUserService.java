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
    /**
     * 注册
     * @param mqttUserRegisterVo
     */
    void register(MqttUserRegisterVo mqttUserRegisterVo);

    /**
     * 校验用户名是否唯一
     * @param username
     */
    void checkUsernameUnique(String username);
}
