package com.wp.sso.service.imp;

import com.wp.common.pojo.E3Result;
import com.wp.mapper.TbUserMapper;
import com.wp.pojo.TbUser;
import com.wp.pojo.TbUserExample;
import com.wp.sso.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * @program: WpMall
 * @description:
 * @author: Pan wu
 * @create: 2018-10-04 15:33
 **/
@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private TbUserMapper tbUserMapper;

    @Override
    public E3Result checkData(String param, int type) {
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        if(type == 1) {
            criteria.andUsernameEqualTo(param);
        }
        else if(type == 2) {
            criteria.andPhoneEqualTo(param);
        }
        else if(type == 3) {
            criteria.andEmailEqualTo(param);
        }
        else {
            return E3Result.build(400, "type is wrong");
        }
        List<TbUser> tbUsers = tbUserMapper.selectByExample(example);
        if(tbUsers != null && tbUsers.size() > 0) {
            return E3Result.ok(false);
        }
        return E3Result.ok(true);
    }

    @Override
    public E3Result register(TbUser tbUser) {
        //check the data again after front-check
        tbUser.setCreated(new Date());
        tbUser.setUpdated(new Date());
        //employ spring util to make password as MD5
        String md5Digest = DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes());
        tbUser.setPassword(md5Digest);
        tbUserMapper.insert(tbUser);
        return E3Result.ok();
    }
}
