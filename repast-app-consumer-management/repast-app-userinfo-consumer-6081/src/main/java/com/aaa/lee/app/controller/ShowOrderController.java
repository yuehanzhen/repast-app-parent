package com.aaa.lee.app.controller;

import com.aaa.lee.app.base.BaseController;
import com.aaa.lee.app.base.ResultData;
import com.aaa.lee.app.service.IRepastService;
import com.aaa.lee.app.vo.OmsOrderAndShopInfoVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(value = "展示订单", tags = "展示订单接口")
public class ShowOrderController extends BaseController {

    @Autowired
    private IRepastService iRepastService;
    @GetMapping("/showOrder")
    public ResultData showOrder(String token, Integer orderStatus){
        List<OmsOrderAndShopInfoVo> omsOrderAndShopInfoVos = iRepastService.showOrder(token,orderStatus);
        if(null!=omsOrderAndShopInfoVos){
            return success("获取订单信息成功",omsOrderAndShopInfoVos);
        }else {
            return failed();
        }
    }
}
