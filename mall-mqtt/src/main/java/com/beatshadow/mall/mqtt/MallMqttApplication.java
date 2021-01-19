package com.beatshadow.mall.mqtt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author gnehcgnaw
 */
@EnableDiscoveryClient
@SpringBootApplication
public class MallMqttApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallMqttApplication.class, args);
    }

}
