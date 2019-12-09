package com.aaa.lee.app.controller;

import com.aaa.lee.app.service.OrderCartService;
import com.aaa.lee.app.service.ProductService;
import com.aaa.lee.app.service.RedisService;
import com.aaa.lee.app.vo.OrderInfoVo;
import com.aaa.lee.app.vo.ProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class OrderInfoController {

    @Autowired
    private RedisService redisService;
    @Autowired
    private OrderCartService orderCartService;
    @Autowired
    private ProductService productService;
    /**
     * 得到订单页面的商品详情,根据购物车状态码
     * @param memberId
     * @return
     */

    @PostMapping("/getOrderInfo")
    public List<OrderInfoVo> getOrderInfo(@RequestParam("token") String token){
        List<OrderInfoVo> orderInfo = orderCartService.getOrderInfo(token);
        return orderInfo;
    }
    /**
     * 根据productId查询商品信息
     * @param products
     * @return
     */
   /* @PostMapping("/getOrderInfoByProductId")
    public List<ProductVo> getOrderInfoByProductId(@RequestBody List<Long> productIds){
        *//*for (Long l:productIds) {
            System.out.println("8081从List集合中遍历出来的"+l.toString());
        }*//*
        ArrayList<ProductVo> productVos = new ArrayList<>();
        for (Long productId:productIds) {
            *//*String s = JSONUtil.toJsonString(productId);
            long l = Long.parseLong(s);*//*
            ProductVo orderInfoByProductId = productService.getOrderInfoByProductId(productId);
            if(null!=orderInfoByProductId){
                productVos.add(orderInfoByProductId);
            }
        }
        return productVos;
    }*/
}
