package com.beatshadow.mall.member.web;

import com.beatshadow.common.utils.R;
import com.beatshadow.mall.member.feign.OrderFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/30 11:52
 */
@Slf4j
@Controller
public class MemberWebController {


    private OrderFeignService orderFeignService ;

    public MemberWebController(OrderFeignService orderFeignService) {
        this.orderFeignService = orderFeignService;
    }

    @GetMapping("/memberOrder.html")
    public String memberOrderPage(@RequestParam(value = "pageNum" ,defaultValue = "1") Integer pageNum , Model model){
        //获取到支付宝过来的数据，可以在这里修改订单的状态，但是这种不好，使用支付的异步回调

        //http://member.mall.com/memberOrder.html?charset=utf-8&out_trade_no=202005301509543151266627881094619137&method=alipay.trade.page.pay.return&total_amount=1.00&sign=Ica5V3zjzk3wKksYWQZA657v7c8My87XUp%2FqYI6duHL76LsNy5DRba3LAjkgImbDBlCCgiIjM5HVidSfpI96AqjsYSitkL68pHhbjJV1%2FwqBYHD2SLSaLtrzsrqRthbj0fBLhS2FlKaP%2Fzc7BH9lw56EERxs6cuf4iCLS%2BGrz2yidFUzkQNAYUzVezmLOcGnrBD5G3VJy6NZEUIILX2lkTwdYWH9p8Xud6Z%2BgvrsvzWJkPQ7G5SJrx8TKJPjPhuAmYagUjQc1ve7oZNcX%2BIYBxp4PeNoBbbWb%2FtkVCD7njFkF0eFxKk8VNieE6hYH7hoaZ38k9XCvonE64RnGM76%2FA%3D%3D&trade_no=2020053022001449190500372988&auth_app_id=2016091800543202&version=1.0&app_id=2016091800543202&sign_type=RSA2&seller_id=2088102176246562&timestamp=2020-05-30+15%3A10%3A52
        //查出当前登录用户的所有列表
        Map<String, Object> params = new HashMap<>();
        params.put("page",pageNum.toString());

        R r = orderFeignService.listWithItem(params);
        model.addAttribute("orders",r);
        log.debug("orders is {}" ,r);
        return "orderList" ;
    }
}
