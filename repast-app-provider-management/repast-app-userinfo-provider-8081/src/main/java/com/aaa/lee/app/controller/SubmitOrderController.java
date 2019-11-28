package com.aaa.lee.app.controller;

import com.aaa.lee.app.base.ResultData;
import com.aaa.lee.app.domain.OmsCartItem;
import com.aaa.lee.app.domain.OmsOrder;
import com.aaa.lee.app.domain.SubmitOrderVO;
import com.aaa.lee.app.service.SubmitOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

/**
 * TODO
 *
 * @data 2019/11/21 23:28
 * @project repast-app-parent
 * @declaration:
 */
@RestController
public class SubmitOrderController {
    @Autowired
    private SubmitOrderService submitOrderService;

    /**
     * 通过店铺id和用户id获取当前用户所选购当前商家的商品
     * @return
     */
    @PostMapping("/getSubmitOrder")
    public List<SubmitOrderVO> getSubmitOrder(@RequestBody OmsCartItem omsCartItem){
        List<SubmitOrderVO> submitOrder = submitOrderService.getSubmitOrder(omsCartItem);
        return submitOrder;
    }

    /**
     * 创建订单
     * @param submitOrderVO
     * @return
     */
    @PostMapping("/setOrder")
    public int setOrder(@RequestBody List<SubmitOrderVO>  submitOrderVO) throws ParseException {
        int i = submitOrderService.setOrder(submitOrderVO);
        return i;
    }
}
