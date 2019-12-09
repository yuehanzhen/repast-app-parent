package com.aaa.lee.app.controller;

import com.aaa.lee.app.service.ShowOrderService;
import com.aaa.lee.app.vo.OmsOrderAndShopInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ShowOrderController {
    @Autowired
    private ShowOrderService showOrderService;

    @GetMapping("/showOrder")
    public List<OmsOrderAndShopInfoVo> showOrder(@RequestParam("token") String token,@RequestParam("orderStatus") Integer orderStatus){
        List<OmsOrderAndShopInfoVo> omsOrderAndShopInfoVos = showOrderService.showOrder(token,orderStatus);
        return omsOrderAndShopInfoVos;
    }
}
