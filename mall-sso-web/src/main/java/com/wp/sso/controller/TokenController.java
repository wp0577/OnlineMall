package com.wp.sso.controller;

import com.wp.common.pojo.E3Result;
import com.wp.common.util.JsonUtils;
import com.wp.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/user/token/{token}", /*不加这句会传回html/txt格式*/produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Object getUserBytoken(@PathVariable String token, String callback) {
        E3Result userByToken = tokenService.getUserByToken(token);
        if(StringUtils.isNotBlank(callback)) {
            //注释方法为老方法，下面方法可以在spring4.1以上版本使用
            //return callback + "(" + JsonUtils.objectToJson(userByToken) + ");";
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(userByToken);
            mappingJacksonValue.setJsonpFunction(callback);
            return mappingJacksonValue;
        }
        return userByToken;
    }


}
