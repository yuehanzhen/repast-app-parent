package com.aaa.lee.app.controller;

import com.aaa.lee.app.base.BaseController;
import com.aaa.lee.app.base.ResultData;
import com.aaa.lee.app.domain.OrderCart;
import com.aaa.lee.app.service.IRepastService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * @author leeoneone
 * @date 2019/11/23 0023 15:12
 */
@RestController
@Api(value = "购物车",tags = "购物车接口")
public class OrderCartController extends BaseController {
    @Autowired
    private IRepastService repastService;

    @GetMapping("/order/selectCartOperation")
    @ApiOperation(value = "cart",notes = "接收前台发送的店铺id")
        public ResultData orderCart(Long shopId, String token){
        System.out.println(shopId);
        List<OrderCart> receiveList = repastService.orderCart(shopId,token);
        if (null != receiveList){
            return success("购物车查询成功",receiveList);
        }else {
            return failed("购物车查询为空");
        }
    }
    @PostMapping("/order/addCartOperation")
    @ApiOperation(value = "cart",notes = "接收前台发送的商品id和商品数量，商品价格")
    public ResultData addCart(Map<String,Object> data, String token){
        Boolean receiveList = repastService.addCart(data,token);
        if ( receiveList){
            return success("添加购物车成功",receiveList);
        }else {
            return failed("添加失败");
        }
    }
    /*清空购物车*/

    @GetMapping("/order/clearCart")
    @ApiOperation(value = "cart",notes = "接收前台发送的店铺id")
    public ResultData clearCart(Long shopId, String token){
        if (repastService.clearCart(shopId,token)){
            return success("清空购物车成功");
        }else {
            return failed("清空购物车失败");
        }
    }
}

