package com.aaa.lee.app.controller;

import com.aaa.lee.app.base.BaseController;
import com.aaa.lee.app.base.ResultData;
import com.aaa.lee.app.domain.Address;
import com.aaa.lee.app.domain.Order;
import com.aaa.lee.app.domain.OrderItem;
import com.aaa.lee.app.domain.OrderReturnApply;
import com.aaa.lee.app.service.IRepastService;
import com.aaa.lee.app.vo.OmsOrderVo;
import com.google.zxing.WriterException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Author:DongMengKe
 * @Date:2019/11/21 002117:01
 * @Version 1.0
 */
@RestController
@Api(value = "订单", tags = "订单接口")
public class OrderController extends BaseController {
    @Autowired
    private IRepastService repastService;

    /**
     * 取消订单
     * @param ordersn
     * @return
     */
    @GetMapping("/cancalOrder")
    @ApiOperation(value = "取消订单", notes = "取消订单")
    public ResultData cancalOrder(String ordersn,String token){
        return repastService.cancalOrder(ordersn,token);
    }

    /**
     * 确认收货
     * @param orderSn
     * @return
     */
    @GetMapping("/affirmReceipt")
    @ApiOperation(value = "确认收货", notes = "确认收货")
    public ResultData affirmReceipt(String orderSn,String token){
        return repastService.affirmReceipt(orderSn,token);

    }

    /**
     * 恢复付款
     * @param ordersn
     * @param openid
     * @return
     */
    @GetMapping("/toRestoreOrder")
    @ApiOperation(value = "恢复下单", notes = "恢复下单")
    public ResultData toRestoreOrder(String ordersn, String openid,String token){
        return repastService.toRestoreOrder(ordersn, openid,token);
    }


    /**
     * 通过订单编号查询订单信息
     * @param orderId
     * @return
     */
    @GetMapping("/getOrder")
    @ApiOperation(value = "退款",notes = "查询订单信息")
    public ResultData getOrderByOrderId(Long orderId) {
        List<OrderItem> orderItemList = repastService.getOrderByOrderId(orderId);
        if (orderItemList.size() > 0) {
            return success(orderItemList);
        } else {
            return failed();
        }

    }

    /**
     * 添加退款原因
     * @param orderReturnApply
     * @return
     */
    @PostMapping("/insertReason")
    @ApiOperation(value = "退款",notes = "添加退款原因")
    public ResultData insertReason(OrderReturnApply orderReturnApply){
        Integer i = repastService.insertReason(orderReturnApply);
        if (i>0){
            return success();
        }else {
            return failed();
        }

    }


    /**
     * 通过订单id查询订单状态
     * @return
     */
    @GetMapping("/getStatus")
    @ApiOperation(value = "退款",notes = "查询退款状态 ")
    public ResultData getStatusByOrderId(Long orderId){
        OrderReturnApply statusByorderId = repastService.getStatusByOrderId(orderId);
        if (null != statusByorderId){
            return success(statusByorderId);
        }else {
            return failed();
        }
    }
    /**
     * 加入订单
     */
    @Autowired
    private IRepastService iRepastService;
    @PostMapping("/addOrder")
    @ApiOperation(value = "添加订单", notes = "加入购物车的数据到订单详情和订单")
    public ResultData addOrder(@RequestBody List<OmsOrderVo> orderVo){
        Boolean aBoolean = iRepastService.addOrder(orderVo);
        if(aBoolean){
            return success("添加订单成功");
        }else {
            return failed("添加订单失败");
        }
    }
    /**
     * 根据是否付款成功的状态码修改订单状态，如果未付款直接修改状态为无效订单
     * 如果statuid为1 改为无效
     * @param statuID
     * @return
     */
    @PostMapping("deleteOrder")
    @ApiOperation(value = "设置无效订单", notes = "未付款的话设置无效订单")
    public ResultData updateOrder(@RequestParam("statuID") Long statuID){
        Boolean aBoolean = iRepastService.updateOrder(statuID);
        if(aBoolean){
            return success("修改状态码成功");
        }else {
            return failed("修改状态码失败");
        }
    }


}
