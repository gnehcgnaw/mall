package com.beatshadow.mall.mqtt.constant;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2021/1/31 18:06
 */
public enum QosConstants {
    /**
     * qos0
     */
    Qos0(0),
    /**
     * qos1
     */
    Qos1(1),
    /**
     * qos2
     */
    Qos2(2);

    QosConstants(int value) {
        this.value = value;
    }

    private final int value ;


}
