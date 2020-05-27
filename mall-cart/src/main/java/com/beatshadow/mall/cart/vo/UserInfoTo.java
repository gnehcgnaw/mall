package com.beatshadow.mall.cart.vo;

import lombok.Data;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/27 14:58
 */
@Data
public class UserInfoTo {
    private Long userId ;

    private String userKey ;

    private boolean tempUser = false;
}
