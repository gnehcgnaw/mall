package com.beatshadow.mall.auth;


import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.Md5Crypt;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/26 01:58
 */
public class MD5Test {

    //抗修改性【利用抗修改性，只要12345 ，加密之后的MD5就是不可变的】  彩虹表 【暴力破解】，故而MD5不能直接进行密码的加密存储
    //强抗碰撞
    //盐值加密：消息摘要，损失元数据，[沙子和沙子混合，你能分清楚哪一个是你的吗，就是这个原理]
    @Test
    public void test(){
        String s = DigestUtils.md5Hex("1234 ");
        String md5Crypt = Md5Crypt.md5Crypt("1234".getBytes(), "$1$0000");

        //验证的时候重新加密，然后比较
        System.out.println(md5Crypt);

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean matches = bCryptPasswordEncoder.matches("123456", "$2a$10$Zk4EM0D.u5AgdoTWjh5pAeiTsbfZRtD.G3rmHLPzGIE1YuAU.4wkG");
        System.out.println(matches);
        String encode = bCryptPasswordEncoder.encode("123456");
        System.out.println(encode);
    }
}
