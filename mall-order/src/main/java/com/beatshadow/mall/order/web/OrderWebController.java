package com.beatshadow.mall.order.web;

import com.alibaba.fastjson.JSON;
import com.beatshadow.common.utils.R;
import com.beatshadow.mall.order.service.OrderService;
import com.beatshadow.mall.order.vo.OrderConfirmVo;
import com.beatshadow.mall.order.vo.OrderSubmitVo;
import com.beatshadow.mall.order.vo.SubmitOrderResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/28 03:04
 */
@Slf4j
@Controller
public class OrderWebController {

    private OrderService orderService ;

    public OrderWebController(OrderService orderService) {
        this.orderService = orderService;
    }


    /**
     * 购物车页面--》订单列表页面
     * @return
     */
    @GetMapping("/toTrade")
    public String toTrade(Model model , HttpServletRequest httpServletRequest) {

        OrderConfirmVo orderConfirmVo = null;
        try {
            orderConfirmVo = orderService.confirmOrder();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            log.debug(e.getMessage());
        }
        model.addAttribute("orderConfirmData",orderConfirmVo);
        return "confirm";
    }

    /**
     * 下单功能
     *      去创建订单
     *      验证令牌
     *      验证价格
     *      锁库存
     *   下单成功：来到支付页面
     *   下单失败：回到订单确认页，提示重新确认订单信息
     * @param orderSubmitVo
     * @return
     */
    @PostMapping("/submitOrder")
    public String submitOrder(OrderSubmitVo orderSubmitVo , Model model , RedirectAttributes redirectAttributes){
        log.debug("订单提交的数据：{}", JSON.toJSONString(orderSubmitVo));
        SubmitOrderResponseVo submitOrderResponseVo= orderService.submitOrder(orderSubmitVo);
        if (submitOrderResponseVo.getCode()==0){
            //下单成功
            //去支付页面
            model.addAttribute("submitOrderResponseVo",submitOrderResponseVo);
            return "pay";
        }else{
            String msg = "下单失败";
            switch (submitOrderResponseVo.getCode()){
                case 1 : msg += "订单信息过期，请刷新再提交"; break;
                case 2 : msg += "订单商品价格发生变化，请确认之后再提交" ; break;
                case 3 : msg += "库存锁定失败，商品库存不足"; break;
            }
            redirectAttributes.addFlashAttribute("msg",msg);
            return "redirect:http://order.mall.com/toTrade";
        }
    }
}
