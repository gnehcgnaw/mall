package com.beatshadow.mall.order.listener;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.beatshadow.mall.order.config.AlipayTemplate;
import com.beatshadow.mall.order.service.OrderService;
import com.beatshadow.mall.order.vo.PayAsyncVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/30 15:16
 */
@Slf4j
@RestController
public class OrderPayedListener {

    private OrderService orderService ;

    private AlipayTemplate alipayTemplate ;
    public OrderPayedListener(OrderService orderService, AlipayTemplate alipayTemplate) {
        this.orderService = orderService;
        this.alipayTemplate = alipayTemplate;
    }

    //https://opendocs.alipay.com/open/203/105286
    @PostMapping("/payed/notify")
    public String handleAlipayed(PayAsyncVo payAsyncVo, HttpServletRequest request) throws UnsupportedEncodingException, AlipayApiException {
        /**
         * {
         * 	"gmt_create": ["2020-05-30 15:41:31"],
         * 	"charset": ["utf-8"],
         * 	"gmt_payment": ["2020-05-30 15:41:42"],
         * 	"notify_time": ["2020-05-30 15:41:43"],
         * 	"subject": ["mall"],
         * 	"sign": ["c8LUZ3Isebf/hP5MubagGjXiMXML3KKpdl74YZveUSTfd4dJriQdJ9slDmb0fSPaZfRLrHUWiS0b6rX+TvCJCz4Rwx9M0rxb7bioEkwjPXTFMVckt0G8MbJIjB4fOD4BlnyYeO97daoD6A49vsJabN5jAki0u6mlyJ8gsI7z5G471/LjkAa+9pL8H7ZvJLr9icXTG22xetoAGLveC4UvhQ/dA7lMBjW+9CBhgheb6p9713thaQfh0VjnsGGrB1gCAlRgq41a2xpoyiCMW4ByLdsAHldYPTVsLcAFnXBSoji56USy0rQJ4Qlh8Dw9/9oVR/EZTCkVHcHOpoMSW3swyw=="],
         * 	"buyer_id": ["2088102176249196"],
         * 	"body": ["mall"],
         * 	"invoice_amount": ["1.00"],
         * 	"version": ["1.0"],
         * 	"notify_id": ["2020053000222154143049190504238157"],
         * 	"fund_bill_list": ["[{\"amount\":\"1.00\",\"fundChannel\":\"ALIPAYACCOUNT\"}]"],
         * 	"notify_type": ["trade_status_sync"],
         * 	"out_trade_no": ["202005301541078641266635739332898817"],       //系统的订单号
         * 	"total_amount": ["1.00"],
         * 	"trade_status": ["TRADE_SUCCESS"],      //交易站台
         * 	"trade_no": ["2020053022001449190500372877"],       //支付宝交易好
         * 	"auth_app_id": ["2016091800543202"],
         * 	"receipt_amount": ["1.00"],
         * 	"point_amount": ["0.00"],
         * 	"app_id": ["2016091800543202"],
         * 	"buyer_pay_amount": ["1.00"],
         * 	"sign_type": ["RSA2"],
         * 	"seller_id": ["2088102176246562"]
         * }
         */
        //验证签名
        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        //调用SDK验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayTemplate.getAlipay_public_key(), alipayTemplate.getCharset(), alipayTemplate.getSign_type());
        if (signVerified){
            //说明这是支付宝的程序
            //success 表示异步回调成功
            String status = orderService.handlePayResult(payAsyncVo);
            return status ;
        }else{

            return "error" ;
        }

    }
}
