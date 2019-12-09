package com.aaa.lee.app.controller;

import com.aaa.lee.app.base.BaseController;
import com.aaa.lee.app.base.ResultData;
import com.aaa.lee.app.service.IRepastService;
import com.aaa.lee.app.vo.OrderInfoVo;
import com.aaa.lee.app.vo.ProductVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(value = "展示订单页面商品信息", tags = "订单接口")
public class OrderInfoController extends BaseController {

    @Autowired
    private IRepastService iRepastService;


    /**
     * 根据delete_status状态码从购物车中查
     * @return
     */
    @PostMapping("/getOrderInfo")
    @ApiOperation(value = "查询购物车的数据加入到订单", notes = "查询购物车的数据加入到订单")
    public ResultData getOrderInfo(String token){
        if(null!=token){
            String s = iRepastService.selectToken(token);
            if(null!=s){
                List<OrderInfoVo> orderInfo = iRepastService.getOrderInfo(token);
                System.out.println("orderInfo"+orderInfo);
                if(null!=orderInfo){
                    return success("查询购物车数据成功",orderInfo);
                }
            }
        }
        return failed("查询购物车数据失败");

    }

}
