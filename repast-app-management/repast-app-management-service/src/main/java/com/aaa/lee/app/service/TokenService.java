package com.aaa.lee.app.service;

import com.aaa.lee.app.mapper.TokenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    @Autowired
    private TokenMapper tokenMapper;
    public String selectToken(String token){
        String s = tokenMapper.selectToken(token);
        if(null!=token){
            return s;
        }else {
            return null;
        }

    }
}
