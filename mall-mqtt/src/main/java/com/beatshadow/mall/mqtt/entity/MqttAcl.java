package com.beatshadow.mall.mqtt.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
@TableName(value = "mqtt_acl")
public class MqttAcl implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 0: deny, 1: allow
     */
    @TableField(value = "`allow`")
    private Integer allow;

    /**
     * IpAddress
     */
    @TableField(value = "ipaddr")
    private String ipaddr;

    /**
     * Username
     */
    @TableField(value = "username")
    private String username;

    /**
     * ClientId
     */
    @TableField(value = "clientid")
    private String clientid;

    /**
     * 1: subscribe, 2: publish, 3: pubsub
     */
    @TableField(value = "`access`")
    private Integer access;

    /**
     * Topic Filter
     */
    @TableField(value = "topic")
    private String topic;

    private static final long serialVersionUID = 1L;
}