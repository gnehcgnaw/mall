package com.beatshadow.mall.cart.controller;

import com.beatshadow.common.utils.R;
import com.beatshadow.mall.cart.interceptor.CartInterceptor;
import com.beatshadow.mall.cart.services.CartService;
import com.beatshadow.mall.cart.services.impl.CartServiceImpl;
import com.beatshadow.mall.cart.vo.Cart;
import com.beatshadow.mall.cart.vo.CartItem;
import com.beatshadow.mall.cart.vo.UserInfoTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/27 14:48
 */
@Controller
public class CartController {

    private CartService cartService ;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }


    @GetMapping("/getCurrentUserCartItems")
    @ResponseBody
    public  R  getCurrentUserCartItems(){
        List<CartItem> cartItemList = cartService.getCurrentUserCartItems();
        return R.ok().put("cartItemList",cartItemList) ;
    }


    /**
     * 勾选购物项目
     * @param skuId
     * @param check  1xuanzhong 0未选中
     * @return
     */
    @GetMapping("/checkItem")
    public String checkItem(@RequestParam("skuId")Long skuId ,@RequestParam("check")Integer check){
        cartService.checkItem(skuId, check);
        return "redirect:http://cart.mall.com/cart.html" ;
    }


    @GetMapping("/countItem")
    public String countItem(@RequestParam("skuId")Long skuId ,@RequestParam("num")Integer num){
        cartService.countItem(skuId, num);
        return "redirect:http://cart.mall.com/cart.html" ;
    }

    //deleteItem
    @GetMapping("/deleteItem")
    public String deleteItem(@RequestParam("skuId")Long skuId ){
        cartService.deleteItem(skuId);
        return "redirect:http://cart.mall.com/cart.html" ;
    }




    /**
     * 首页购物车
     * 登录：session 有
     * 没登录：按照cookie里面带来的user-key
     * 第一次：如果没有临时用户，帮忙参见一个临时用户，
     *              写一个拦截器
     * @see com.beatshadow.mall.cart.interceptor.CartInterceptor
     * @param  //httpSession
     * @return
     */
    @GetMapping("/cart.html")
    public String cartListPage(
           // HttpSession httpSession
           Model model
    ){
       /* Object loginUser = httpSession.getAttribute(AuthServerConstant.LOGIN_USER);
        if (loginUser==null){
            //没登录,获取临时购物车
            // jd 临时身份 cookie  user-key [一个月的时间] 登录之后也在，
            // user-key标示用户身份，第一次没有user-key会给生产一个，以后每一个请求都有这个身份标示
        }else{
            //登录，获取登录购物车
        }*/

        //使用ThreadLocal 来共享数据
        Cart cart = cartService.getCart();
        model.addAttribute("cartList",cart);
        return "cartList";
    }

    /**
     * 添加商品到购物车，添加成功跳转到 success页面
     * @return
     */
    @GetMapping("/addToCart")
    public String addToCart(@RequestParam("skuId")Long skuId , @RequestParam("num")Integer num, RedirectAttributes redirectAttributes){
        cartService.addToCart(skuId,num);
        //直接跳转到success，有重复刷新重复添加的问题,所以中间重定向一次
        redirectAttributes.addAttribute("skuId",skuId);
        return "redirect:http://cart.mall.com/addToCartSuccess.html" ;
    }

    @GetMapping("/addToCartSuccess.html")
    public String addToCartSuccessPage(@RequestParam("skuId")Long skuId ,Model model){
        //重定向到成功页面，再次查询购物车数据即可
        CartItem cartItem = cartService.getCartItem(skuId);
        model.addAttribute("item",cartItem);
        return "success" ;
    }
}
