package com.aaa.lee.app.controller;

import com.aaa.lee.app.base.BaseController;
import com.aaa.lee.app.base.ResultData;
import com.aaa.lee.app.service.IRepastService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author:DongMengKe
 * @Date:2019/11/25 002521:47
 * @Version 1.0
 */
@RestController
@Api(value = "支付", tags = "支付接口")
public class WXPayController extends BaseController {
    @Autowired
    private IRepastService repastService;

    /**
     * 微信支付接口
     * @param ordersn
     * @param openid
     * @param amount
     * @return
     */
    @GetMapping("/pay")
    @ApiOperation(value = "支付接口", notes = "支付接口")
    public @ResponseBody
    ResultData pay(String ordersn, String openid, Float amount){
        Map<String, Object> result= repastService.pay(ordersn,openid,amount);
        System.out.println(result.get("msg")+"+++++"+result.get("data"));
        if(result!=null){
            return success("支付成功",result);
        }else {
            return failed("支付失败");
        }
    }

    /**
     * 微信支付的回调地址
     * @throws Exception
     */
    @PostMapping("/wxNotify")
    @ApiOperation(value = "回调接口", notes = "回调接口")
    public void wxNotify() throws Exception {
        repastService.wxNotify();
    }
}
