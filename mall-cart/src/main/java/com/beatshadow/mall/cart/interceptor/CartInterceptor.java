package com.beatshadow.mall.cart.interceptor;

import com.beatshadow.common.constant.AuthServerConstant;
import com.beatshadow.common.constant.CartConstant;
import com.beatshadow.common.to.MemberRespondVo;
import com.beatshadow.mall.cart.vo.UserInfoTo;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/27 14:56
 */
public class CartInterceptor  implements HandlerInterceptor {

    public static ThreadLocal<UserInfoTo> userInfoToThreadLocal = new ThreadLocal<>();
    /**
     * 目标方法执行之前
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        UserInfoTo userInfoTo = new UserInfoTo() ;
        MemberRespondVo memberRespondVo = (MemberRespondVo) session.getAttribute(AuthServerConstant.LOGIN_USER);

        if (memberRespondVo!=null){
            userInfoTo.setUserId(memberRespondVo.getId());
        }
        Cookie[] cookies = request.getCookies();
        if (cookies!=null&&cookies.length>0){
            for (Cookie cookie : cookies) {
                String cookieName = cookie.getName();
                if (cookieName.equals(CartConstant.TEMP_USER_COOKIE_NAME)){
                    userInfoTo.setUserKey(cookie.getValue());
                    userInfoTo.setTempUser(true);
                }
            }

        }
        //如果没有临时用户，一定分配一个临时用户
        if (StringUtils.isEmpty(userInfoTo.getUserKey())){
            String uuid = UUID.randomUUID().toString();
            userInfoTo.setUserKey(uuid);
        }
        userInfoToThreadLocal.set(userInfoTo);
        return true;
    }

    /**
     * 业务执行之后，
     *      让浏览器保存一个cookie，保存一个月的时间
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserInfoTo userInfoTo = userInfoToThreadLocal.get();
        if (!userInfoTo.isTempUser()){
            //持续的延长
            Cookie cookie = new Cookie(CartConstant.TEMP_USER_COOKIE_NAME, userInfoTo.getUserKey());
            cookie.setDomain("mall.com");
            cookie.setMaxAge(CartConstant.TEMP_USER_COOKIE_TIMEOUT);
            response.addCookie(cookie);
        }

    }
}
