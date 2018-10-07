package com.wp.sso.service.imp;

import com.wp.common.jedis.JedisClient;
import com.wp.common.pojo.E3Result;
import com.wp.common.util.JsonUtils;
import com.wp.pojo.TbUser;
import com.wp.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private JedisClient jedisClient;

    @Override
    public E3Result getUserByToken(String token) {
        //从url中取参数。
        //根据token查询redis。
        String s = jedisClient.get("SESSION:" + token);
        if (StringUtils.isBlank(s)) {
            //如果查询不到数据。返回用户已经过期。
            return E3Result.build(202, "user has been expired");
        }
        //如果查询到数据，说明用户已经登录。
        //需要重置key的过期时间。
        jedisClient.expire("SESSION:" + token, 1800);
        //把json数据转换成TbUser对象，然后使用e3Result包装并返回。
        TbUser tbUser = JsonUtils.jsonToPojo(s, TbUser.class);
        return E3Result.ok(tbUser);
    }
}
