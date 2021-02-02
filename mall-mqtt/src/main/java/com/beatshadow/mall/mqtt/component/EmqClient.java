package com.beatshadow.mall.mqtt.component;

import com.beatshadow.mall.mqtt.config.MqttProperties;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2021/1/27 22:49
 */
@Slf4j
@Component
public class EmqClient {


    private final MqttProperties mqttProperties ;

    public EmqClient(MqttProperties mqttProperties) {
        this.mqttProperties = mqttProperties;
    }
    private IMqttClient mqttClient ;
    /**
     * 初始化
     */
    @PostConstruct
    public void  init (){
        MqttClientPersistence memoryPersistence = new MemoryPersistence();
        mqttProperties.setClientId(MqttClient.generateClientId());
        try {
            mqttClient = new MqttClient(mqttProperties.getSeverUrl(),mqttProperties.getClientId(),memoryPersistence);
        } catch (MqttException e) {
           log.error("init error : ",e);
        }
    }
    /**
     * 连接
     */
    public void connect (){
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);

    }
}
