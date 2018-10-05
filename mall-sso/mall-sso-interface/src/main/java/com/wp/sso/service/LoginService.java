package com.wp.sso.service;

import com.wp.common.pojo.E3Result;

public interface LoginService {
    E3Result login(String username, String password);
}
