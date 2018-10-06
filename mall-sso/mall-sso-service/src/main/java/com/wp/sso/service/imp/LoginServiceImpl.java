package com.wp.sso.service.imp;

import com.wp.common.pojo.E3Result;
import com.wp.common.util.JsonUtils;
import com.wp.mapper.TbUserMapper;
import com.wp.pojo.TbUser;
import com.wp.pojo.TbUserExample;
import com.wp.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private TbUserMapper tbUserMapper;
    @Autowired
    private JedisClient jedisClient;

    @Override
    public E3Result login(String username, String password) {
        //判断用户名密码是否正确。
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> tbUsers = tbUserMapper.selectByExample(example);
        if(tbUsers != null && tbUsers.size() == 0) {
            return E3Result.build(400, "username or password is wrong");
        }
        TbUser tbUser = tbUsers.get(0);
        if(!tbUser.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
            return E3Result.build(400, "username or password is wrong");
        }
        //登录成功后生成token。Token相当于原来的jsessionid，字符串，可以使用uuid。
        String token = UUID.randomUUID().toString();
        //把用户信息保存到redis。Key就是token，value就是TbUser对象转换成json。
        //使用String类型保存Session信息。可以使用“前缀:token”为key
        tbUser.setPassword(null);
        jedisClient.set("SESSION:"+token, JsonUtils.objectToJson(tbUser));
        //设置key的过期时间。模拟Session的过期时间。一般半个小时。
        jedisClient.expire("SESSION:"+token,1800);
        //返回e3Result包装token。
        return E3Result.ok(token);
    }
}
