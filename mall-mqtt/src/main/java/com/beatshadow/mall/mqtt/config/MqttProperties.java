package com.beatshadow.mall.mqtt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2021/1/31 18:13
 */
@Component
@ConfigurationProperties(prefix = "mqtt")
@Data
public class MqttProperties {
    /**
     * emq broker server url
     */
    private String severUrl = "ws://localhost:8083/mqtt";
    /**
     * application client id
     */
    private String clientId ;
}
