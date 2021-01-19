package com.beatshadow.mall.mqtt.controller;

import com.beatshadow.common.exception.BizCodeEnume;
import com.beatshadow.common.utils.R;
import com.beatshadow.mall.mqtt.exception.UsernameExistException;
import com.beatshadow.mall.mqtt.service.MqttUserService;
import com.beatshadow.mall.mqtt.vo.MqttUserRegisterVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2021/1/19 16:18
 */
@RequestMapping("/mqtt")
@Controller
public class MqttUserController {
    private final MqttUserService mqttUserService ;

    public MqttUserController(MqttUserService mqttUserService) {
        this.mqttUserService = mqttUserService;
    }

    /**
     * 用户注册
     * @param mqttUserRegisterVo
     * @return
     */
    @PostMapping("/register")
    public R register(@RequestBody MqttUserRegisterVo mqttUserRegisterVo) {
        try{
            mqttUserService.register(mqttUserRegisterVo);
        }catch (UsernameExistException usernameExistException){
            return R.error(BizCodeEnume.USER_EXIST_EXCEPTION.getCode(),BizCodeEnume.USER_EXIST_EXCEPTION.getMsg());

        }
        return R.ok() ;
    }
}
