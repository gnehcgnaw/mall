package com.beatshadow.mall.mqtt.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2021/1/19 16:09
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "mqtt_user")
public class MqttUser implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "username")
    private String username;

    @TableField(value = "`password`")
    private String password;

    @TableField(value = "salt")
    private String salt;

    @TableField(value = "is_superuser")
    private Boolean isSuperuser;

    @TableField(value = "created")
    private Date created;

    private static final long serialVersionUID = 1L;
}