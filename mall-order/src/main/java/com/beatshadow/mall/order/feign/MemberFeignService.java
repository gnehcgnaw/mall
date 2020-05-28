package com.beatshadow.mall.order.feign;

import com.beatshadow.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/28 11:56
 */
@FeignClient(value = "mall-member")
public interface MemberFeignService {

    @RequestMapping("/member/memberreceiveaddress/get-by-member-id/{id}")
    public R getMemberReceiveAddressListByMemberId(@PathVariable("id") Long id);

}
