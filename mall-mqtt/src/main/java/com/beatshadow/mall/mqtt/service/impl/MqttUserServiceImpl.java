package com.beatshadow.mall.mqtt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beatshadow.mall.mqtt.exception.UsernameExistException;
import com.beatshadow.mall.mqtt.vo.MqttUserRegisterVo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.beatshadow.mall.mqtt.dao.MqttUserMapper;
import com.beatshadow.mall.mqtt.entity.MqttUser;
import com.beatshadow.mall.mqtt.service.MqttUserService;

import java.util.Date;

/**
 * 
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2021/1/19 16:09
 */
@Service
public class MqttUserServiceImpl extends ServiceImpl<MqttUserMapper, MqttUser> implements MqttUserService{

    @Override
    public void register(MqttUserRegisterVo mqttUserRegisterVo) {
        MqttUser mqttUser = new MqttUser() ;
        //检查用户名的唯一性【异常处理】
        String userName = mqttUserRegisterVo.getUserName();
        checkUsernameUnique(userName);
        //添加到数据库
        //password 加密不可逆 ，MD5【信息摘要算法】 & MD5 盐值加密
        //MD5 盐值加密 ： 通过生成随机数与MD5生成字符串进行组合 ，数据库同时存储MD5值与salt值，验证正确性时使用salt进行MD5即可。
        //Spring提供的，自带盐值的MD5加密 BCryptPasswordEncoder
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(mqttUserRegisterVo.getPassword());
        mqttUser.setUsername(userName);
        mqttUser.setPassword(encode);
        mqttUser.setCreated(new Date());
        this.baseMapper.insert(mqttUser);
    }

    @Override
    public void checkUsernameUnique(String username) {
        Integer count = this.baseMapper.selectCount(new QueryWrapper<MqttUser>().eq("username", username));
        if (count>0){
            throw new UsernameExistException();
        }
    }
}
