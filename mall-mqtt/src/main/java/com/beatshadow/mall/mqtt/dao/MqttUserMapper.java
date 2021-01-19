package com.beatshadow.mall.mqtt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.beatshadow.mall.mqtt.entity.MqttUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2021/1/19 16:09
 */
@Mapper
public interface MqttUserMapper extends BaseMapper<MqttUser> {
}