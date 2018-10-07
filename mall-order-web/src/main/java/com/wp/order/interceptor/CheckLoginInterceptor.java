package com.wp.order.interceptor;

import com.wp.cart.service.CartService;
import com.wp.common.pojo.E3Result;
import com.wp.common.util.CookieUtils;
import com.wp.common.util.JsonUtils;
import com.wp.pojo.TbItem;
import com.wp.pojo.TbUser;
import com.wp.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CheckLoginInterceptor implements HandlerInterceptor {

    @Value("${COOKIE_TOKEN_KEY}")
    private String COOKIE_TOKEN_KEY;
    @Value("${CART_COOKIE}")
    private String CART_COOKIE;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private CartService cartService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        //从cookie中取token
        String cookieValue = CookieUtils.getCookieValue(request, COOKIE_TOKEN_KEY, true);
        //如果没有取到，没有登录，跳转到sso系统的登录页面。拦截
        if (StringUtils.isBlank(cookieValue)) {
            //当登录后需要直接跳转到目前的订单页面，所以使用redirect作为参数传到login页面
            //需要使用request.getRequestURL()，而不是URI，URI只取ip后面的地址
            response.sendRedirect("http://localhost:8088/page/login?redirect=" + request.getRequestURL());
            return false;
        }
        //如果取到token。判断登录是否过期，需要调用sso系统的服务，根据token取用户信息
        E3Result userByToken = tokenService.getUserByToken(cookieValue);
        //如果没有取到用户信息，登录已经过期，重新登录。跳转到登录页面。拦截
        if (!userByToken.getStatus().equals(200)) {
            response.sendRedirect("http://localhost:8088/page/login?redirect=" + request.getRequestURL());
            return false;
        }
        //如果取到用户信息，用户已经是登录状态，把用户信息保存到request中。放行
        TbUser user = (TbUser) userByToken.getData();
        request.setAttribute("user", user);
        //判断cookie中是否有购物车信息，如果有合并购物车
        String cartCookies = CookieUtils.getCookieValue(request, CART_COOKIE, true);
        if (StringUtils.isNotBlank(cartCookies)) {
            System.out.println("cartService : " + cartService);
            System.out.println("user : " + user);
            cartService.mergeCart(user.getId(), JsonUtils.jsonToList(cartCookies, TbItem.class));
            CookieUtils.setCookie(request, response, CART_COOKIE, "");
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
