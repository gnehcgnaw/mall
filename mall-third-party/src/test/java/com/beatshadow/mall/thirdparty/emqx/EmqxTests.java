package com.beatshadow.mall.thirdparty.emqx;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2021/1/7 17:39
 */
public class EmqxTests {
    public static void main(String[] args) {
        String subTopic = "testtopic/#" ;
        String pubTopic = "testtopic/1" ;
        String content = "Hello World" ;
        int qos = 2;
        String broker = "tcp://broker.emqx.io:1883";
        String clientId = "emqx_test";

        MemoryPersistence persistence = new MemoryPersistence() ;
        try {
            MqttClient mqttClient = new MqttClient(broker, clientId, persistence);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
