package com.beatshadow.mall.order.web;

import com.alipay.api.AlipayApiException;
import com.beatshadow.mall.order.config.AlipayTemplate;
import com.beatshadow.mall.order.service.OrderService;
import com.beatshadow.mall.order.vo.PayVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/30 02:55
 */
@Controller
public class PayWebController {

    private AlipayTemplate alipayTemplate ;

    private OrderService orderService ;
    public PayWebController(AlipayTemplate alipayTemplate, OrderService orderService) {
        this.alipayTemplate = alipayTemplate;
        this.orderService = orderService;
    }

    /**
     * 给浏览器返回支付宝支付页面
     * @param orderSn
     * @return
     * @throws AlipayApiException
     */
    @ResponseBody
    @GetMapping(value = "/payOrder" ,produces = "text/html")
    public String payOrder(@RequestParam("orderSn") String orderSn) throws AlipayApiException {

        PayVo payVo = orderService.getOrderPay(orderSn);
        //返回的是页面，将此页面直接交给浏览器
       // 沙厢测试环境,高额的限制原因，将此设置为1
        payVo.setTotal_amount("1");
        String pay = alipayTemplate.pay(payVo);
        return  pay ;
    }
}
