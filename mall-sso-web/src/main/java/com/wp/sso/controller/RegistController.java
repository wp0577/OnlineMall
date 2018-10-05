package com.wp.sso.controller;

import com.wp.common.pojo.E3Result;
import com.wp.pojo.TbUser;
import com.wp.sso.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: WpMall
 * @description: Register controller
 * @author: Pan wu
 * @create: 2018-10-04 15:28
 **/
@Controller
public class RegistController {

    @Autowired
    private RegisterService registerService;

    @RequestMapping("/page/register")
    public String regiPage(){
        return "register";
    }

    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public E3Result checkData(@PathVariable String param, @PathVariable Integer type) {
        //call up registService to check data
        //type=1:username 2:phone 3:mail
        E3Result e3Result = registerService.checkData(param, type);
        return e3Result;
    }

    @RequestMapping("/user/register")
    @ResponseBody
    //call up tbUser to accept the param from web
    public E3Result regist(TbUser tbUser) {
        E3Result register = registerService.register(tbUser);
        return register;
    }

}
