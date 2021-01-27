package com.beatshadow.mall.mqtt.utils;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2021/1/19 17:18
 */
public class BCryptPasswordEncoderTest {

    @Test
    public void crateBCryptPasswordEncoder(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode("public");
        System.out.println(encode);


        boolean matches = new BCryptPasswordEncoder().matches("sub1", "$2a$10$4j3B6IbMD2AZdzaIKaFje.crPbFkxVODhf60O9Yh7Or9yrcyBBew.");
        System.out.println(matches);
    }
}
