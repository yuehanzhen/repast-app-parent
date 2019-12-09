package com.aaa.lee.app.controller;

import com.aaa.lee.app.domain.MemberDefaultAddress;
import com.aaa.lee.app.service.MemberDefaultAddressService;
import com.aaa.lee.app.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultAddressController {

    @Autowired
    private MemberDefaultAddressService memberDefaultAddressService;

    /**
     * 根据默认用户Id查询默认地址
     * @return
     */
    @GetMapping("/getDefaultAddress")
    public MemberDefaultAddress getDefaultAddress(@RequestParam("token") String token){
        MemberDefaultAddress memberDefaultAddress = memberDefaultAddressService.getMemberDefaultAddress(token);
        return memberDefaultAddress;
    }
}
