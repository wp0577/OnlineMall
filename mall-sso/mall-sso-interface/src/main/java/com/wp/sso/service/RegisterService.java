package com.wp.sso.service;

import com.wp.common.pojo.E3Result;
import com.wp.pojo.TbUser;

/**
 * @program: WpMall
 * @description:
 * @author: Pan wu
 * @create: 2018-10-04 15:32
 **/
public interface RegisterService {
    E3Result checkData(String param, int type);
    E3Result register(TbUser tbUser);
}
