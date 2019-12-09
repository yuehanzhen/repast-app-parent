package com.aaa.lee.app.controller;

import com.aaa.lee.app.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    @Autowired
    private TokenService tokenService;
    @GetMapping("/token")
    public String selectToken(@RequestParam("token") String token){
        String s = tokenService.selectToken(token);
        return s;
    }
}
