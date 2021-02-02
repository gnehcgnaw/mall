package com.beatshadow.mall.mqtt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * EMQ X 的 HTTP API 使用 Basic 认证 (opens new window)方式
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2021/1/31 17:52
 */
@ConfigurationProperties(prefix = "emq.application")
@Component
@Data
public class EmqApplicationProperties {

    private String id ;

    private String secret ;
}
