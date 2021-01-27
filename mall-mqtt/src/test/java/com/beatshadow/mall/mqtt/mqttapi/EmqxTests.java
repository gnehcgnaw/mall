package com.beatshadow.mall.mqtt.mqttapi;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;


/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2021/1/20 10:46
 */
public class EmqxTests {
    @Test
    public void sub(){
        //订阅主题过滤器
        String subTopic = "/testtopic0";
        //地址
        String broker = "tcp://localhost:1883";
        //clinetId
        String clientId = "emqx_test_sub";
        MemoryPersistence memoryPersistence = new MemoryPersistence();
        try {
            MqttClient mqttClient = new MqttClient(broker,clientId,memoryPersistence);

            //MQTT 连接选项
            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setUserName("sub_0");
            mqttConnectOptions.setPassword("sub_0".toCharArray());
            mqttConnectOptions.setCleanSession(true);


            //建立连接
            mqttClient.connect(mqttConnectOptions);
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    // 连接丢失后，一般在这里面进行重连
                    System.out.println("连接断开，可以做重连");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    if (!message.isDuplicate()) {
                        // subscribe后得到的消息会执行到这里面
                        System.out.println("接收消息主题:" + topic);
                        System.out.println("接受消息Id:" + message.getId());
                        System.out.println("接收消息Qos:" + message.getQos());
                        System.out.println("接收消息内容:" + new String(message.getPayload()));
                    }
                }
                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    System.out.println("deliveryComplete---------" + token.isComplete());
                }
            });
            //订阅
            mqttClient.subscribe(subTopic,2);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void pub(){
        //发布主题
        String pubTopic = "/testtopic0";
        //主题内容
        String content = "Hello World _2";
        //地址
        String broker = "tcp://localhost:1883";
        //clinetId
        String clientId = "emqx_test_pub";
        MemoryPersistence memoryPersistence = new MemoryPersistence();
        try {
            MqttClient mqttClient = new MqttClient(broker,clientId,memoryPersistence);
            //MQTT 连接选项
            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setUserName("pub_0");
            mqttConnectOptions.setPassword("pub_0".toCharArray());
            mqttConnectOptions.setCleanSession(true);
            //建立连接
            mqttClient.connect(mqttConnectOptions);
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    // 连接丢失后，一般在这里面进行重连
                    System.out.println("连接断开，可以做重连");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    if (!message.isDuplicate()) {
                        // subscribe后得到的消息会执行到这里面
                        System.out.println("接收消息主题:" + topic);
                        System.out.println("接受消息Id:" + message.getId());
                        System.out.println("接收消息Qos:" + message.getQos());
                        System.out.println("接收消息内容:" + new String(message.getPayload()));
                    }
                }
                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    System.out.println("deliveryComplete---------" + token.isComplete());
                }
            });

            //发布
            MqttMessage mqttMessage = new MqttMessage(content.getBytes(StandardCharsets.UTF_8));
            mqttMessage.setQos(2);
            mqttMessage.setRetained(true);
            mqttClient.publish(pubTopic,mqttMessage);
            System.out.println("pub success");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


}
