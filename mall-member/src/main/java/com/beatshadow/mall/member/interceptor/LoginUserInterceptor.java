package com.beatshadow.mall.member.interceptor;

import com.beatshadow.common.constant.AuthServerConstant;
import com.beatshadow.common.to.MemberRespondVo;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/28 03:08
 */
public class LoginUserInterceptor  implements HandlerInterceptor {
    public static ThreadLocal<MemberRespondVo> userInfoToThreadLocal = new ThreadLocal<>();
    /**
     * 目标方法执行之前
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        HttpSession session = request.getSession();
        MemberRespondVo memberRespondVo = (MemberRespondVo) session.getAttribute(AuthServerConstant.LOGIN_USER);

        if (memberRespondVo!=null){
            userInfoToThreadLocal.set(memberRespondVo);
            return true ;
        }else {
            response.sendRedirect("http://auth.mall.com/login.html");
            return false ;
        }
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
/*    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserInfoTo userInfoTo = userInfoToThreadLocal.get();
        if (!userInfoTo.isTempUser()){
            //持续的延长
            Cookie cookie = new Cookie(CartConstant.TEMP_USER_COOKIE_NAME, userInfoTo.getUserKey());
            cookie.setDomain("mall.com");
            cookie.setMaxAge(CartConstant.TEMP_USER_COOKIE_TIMEOUT);
            response.addCookie(cookie);
        }

    }*/
}
