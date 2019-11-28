package com.aaa.lee.app.controller;

import com.aaa.lee.app.base.BaseController;
import com.aaa.lee.app.domain.Address;
import com.aaa.lee.app.service.AddressService;
import com.aaa.lee.app.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author:DongMengKe
 * @Date:2019/11/21 002123:48
 * @Version 1.0
 */
@RestController
public class AddressController extends BaseController {
    @Autowired
    private AddressService addressService;
    @Autowired
    private RedisService redisService;
    /**
     * 获取用户订单的收货地址
     * @param memberid
     * @return
     */
    @GetMapping("/getMemberAddressByMemberId")
    public List<Address> getMemberAddressByMemberId(@RequestParam("memberid") Long memberid){
        List<Address> addressList = addressService.getAllAddressbyId(memberid);
        if(null!=addressList){
            return addressList;
        }
        return null;
    }

    /**
     * 查询用户默认收货地址
     * @return
     */
    @GetMapping("/getAddressByPrimaryId")
    public Address getAddressByPrimaryId(){
        try {
            Address address = addressService.getDefaultAddress(redisService);
            if(null!=address){
                return address;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
