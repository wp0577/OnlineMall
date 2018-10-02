package com.wp.search.controller.com.wp.search.Exeception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: WpMall
 * @description: springMVC global exception handler
 * @author: Pan wu
 * @create: 2018-10-02 15:02
 **/
public class MyExeception implements HandlerExceptionResolver {

    Logger logger = LoggerFactory.getLogger(MyExeception.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        //写日志文件
        logger.error("系统发生异常", e);
        //发邮件、发短信
        //Jmail：可以查找相关的资料
        //需要在购买短信。调用第三方接口即可。
        //展示错误页面
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message", "系统发生异常，请稍后重试");
        modelAndView.setViewName("error/exception");
        return modelAndView;
    }
}
