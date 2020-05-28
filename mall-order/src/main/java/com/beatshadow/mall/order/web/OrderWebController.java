package com.beatshadow.mall.order.web;

import com.beatshadow.mall.order.service.OrderService;
import com.beatshadow.mall.order.vo.OrderConfirmVo;
import com.netflix.client.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
