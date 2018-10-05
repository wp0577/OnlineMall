package com.wp.sso.service;

import com.wp.common.pojo.E3Result;

public interface TokenService {
    E3Result getUserByToken(String token);
}
