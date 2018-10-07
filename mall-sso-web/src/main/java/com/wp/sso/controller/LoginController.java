package com.wp.sso.controller;

import com.wp.common.pojo.E3Result;
import com.wp.common.util.CookieUtils;
import com.wp.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: WpMall
 * @description: login controller
 * @author: Pan wu
 * @create: 2018-10-04 16:04
 **/
@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    //接受redirect参数，用来回调页面
    @RequestMapping("/page/login")
    public String loginPage(String redirect, Model model) {
        model.addAttribute("redirect", redirect);
        return "login";
    }

    @RequestMapping("/user/login")
    @ResponseBody
    public E3Result login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        E3Result login = loginService.login(username, password);
        String token = login.getData().toString();
        CookieUtils.setCookie(request, response, "COOKIE_TOKEN_KEY", token);
        return login;
    }

}
