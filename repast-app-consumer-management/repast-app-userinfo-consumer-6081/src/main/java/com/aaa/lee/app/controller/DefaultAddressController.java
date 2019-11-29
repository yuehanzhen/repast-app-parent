package com.aaa.lee.app.controller;

import com.aaa.lee.app.base.BaseController;
import com.aaa.lee.app.base.ResultData;
import com.aaa.lee.app.domain.MemberDefaultAddress;
import com.aaa.lee.app.service.IRepastService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "默认地址", tags = "默认地址接口")
public class DefaultAddressController extends BaseController {

    /**
     * 根据用户默认Id获取默认收货地址
     */
    @Autowired
    private IRepastService iRepastService;
    @GetMapping("/getDefaultAddress")
    @ApiOperation(value = "默认收获地址", notes = "获取会员默认收获地址")
    public ResultData getDefaultAddress(){
        MemberDefaultAddress defaultAddress = iRepastService.getDefaultAddress();
        if(null!=defaultAddress){
            return success("查询默认地址成功",defaultAddress);
        }else{
            return failed();
        }
    }

}
