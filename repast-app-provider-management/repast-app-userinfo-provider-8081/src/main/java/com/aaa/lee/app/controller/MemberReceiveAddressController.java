package com.aaa.lee.app.controller;

import com.aaa.lee.app.domain.MemberReceiveAddress;
import com.aaa.lee.app.service.MemberReceiveAddressService;
import com.aaa.lee.app.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Company AAA软件教育
 * @Author Seven Lee
 * @Date Create in 2019/11/21 10:38
 * @Description
 **/
@RestController
public class MemberReceiveAddressController {

    @Autowired
    private MemberReceiveAddressService memberReceiveAddressService;
    @Autowired
    private RedisService redisService;

    /**
     * @author Seven Lee
     * @description
     *      通用会员id获取会员收获地址列表
     * @param []
     * @date 2019/11/21
     * @return java.util.List<com.aaa.lee.app.domain.MemberReceiveAddress>
     * @throws 
    **/
    @GetMapping("/receive")
    public List<MemberReceiveAddress> getMemberReceiveAddress() {
        return memberReceiveAddressService.getMemberReceiveAddress(redisService);
    }

}
