package com.wp.cart.interceptor;

import com.wp.common.pojo.E3Result;
import com.wp.common.util.CookieUtils;
import com.wp.pojo.TbUser;
import com.wp.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserInterceptor implements HandlerInterceptor {

    @Value("${COOKIE_TOKEN_KEY}")
    private String COOKIE_TOKEN_KEY;
    @Autowired
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        //实现一个HandlerInterceptor接口。
        //在执行handler方法之前做业务处理
        //从cookie中取token。使用CookieUtils工具类实现。
        String cookieValue = CookieUtils.getCookieValue(request, COOKIE_TOKEN_KEY);
        //没有取到token，用户未登录。放行
        if(StringUtils.isBlank(cookieValue)) return true;
        //取到token，调用sso系统的服务，根据token查询用户信息。
        E3Result userByToken = tokenService.getUserByToken(cookieValue);
        //没有返回用户信息。登录已经过期，未登录，放行。
        if(userByToken.getStatus() != 200) {
            return true;
        }
        //返回用户信息。用户是登录状态。可以把用户对象保存到request中，在Controller中可以通过判断request中是否包含用户对象，确定是否为登录状态。
        TbUser data = (TbUser) userByToken.getData();
        request.setAttribute("user", data);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
