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
     * 查询会员的收货地址列表
     * @return
     */
    @GetMapping("/getMemberAddressByMemberId")
    @ApiOperation(value = "查询用户的收货地址列表", notes = "查询收货地址列表")
    public ResultData getMemberAddressByMemberId(Long memberid){
        List<Address> addressList = repastService.getMemberAddressByMemberId(memberid);
        if(addressList.size()>0) {
            return success("查询成功", addressList);
        }else {
            return failed("查询失败");
        }
    }

    /**
     * 通过主键id查询默认收货地址
     * @return
     */
//    @GetMapping("/getAddressByPrimaryId")
//    @ApiOperation(value = "查询用户的收货地址", notes = "查询收货地址")
//    public ResultData getAddressByPrimaryId(){
//        Address address = repastService.getAddressByPrimaryId();
//        if(null!=address){
//            return success("查询成功",address);
//        }
//        return failed("查询失败");
//    }

    /**
     * 提交订单
     * @return
     */
    @GetMapping("/submitOrder")
    @ApiOperation(value = "提交订单", notes = "保存订单")
    public ResultData submitOrder(){
        Map<String, Object> b = repastService.submitOrder();
        if(b!=null && "200".equals(b.get("code"))){
            return success("提交订单成功");
        }
        return failed("提交订单失败");
    }
    /**
     * 生成二维码
     * @param ordersn
     * @param payamout
     * @throws WriterException
     * @throws IOException
     */
//    @GetMapping("/api/wxpay")
//    @ApiOperation(value = "支付二维码", notes = "支付二维码")
//    public ResultData generateCode(String ordersn,BigDecimal payamout) throws IOException, WriterException {
//        boolean b = repastService.generateCode(ordersn, payamout);
//        if(b!=false){
//            return success("二维码生成成功");
//        }else{
//            return failed("失败");
//        }
//    }

    /**
     * 统一下单接口
     * @param openid
     * @return
     */
//    @RequestMapping(value = "/wxPay", method = RequestMethod.GET)
//    @ApiOperation(value = "微信支付接口", notes = "微信支付接口")
//    public ResultData wxPay(String openid){
//        Map<String, Object> map = repastService.wxPay(openid);
//        if(map!=null){
//            return success("请求成功",map);
//
//        }else {
//            return failed();
//        }
//    }

    /**
     * 取消订单
     * @param ordersn
     * @return
     */
    @GetMapping("/cancalOrder")
    @ApiOperation(value = "取消订单", notes = "取消订单")
    public ResultData cancalOrder(String ordersn){
        Boolean aBoolean = repastService.cancalOrder(ordersn);
        if(aBoolean==true){
            return success("取消订单成功");
        }else {
            return failed("取消订单失败");
        }
    }

    /**
     * 确认收货
     * @param orderSn
     * @return
     */
    @GetMapping("/affirmReceipt")
    @ApiOperation(value = "确认收货", notes = "确认收货")
    public ResultData affirmReceipt(String orderSn){
        Boolean aBoolean = repastService.affirmReceipt(orderSn);
        if(aBoolean==true){
            return success("收货成功");
        }else {
            return failed("收货失败");
        }
    }

    /**
     * 恢复付款
     * @param ordersn
     * @param openid
     * @return
     */
    @GetMapping("/toRestoreOrder")
    @ApiOperation(value = "恢复下单", notes = "恢复下单")
    public ResultData toRestoreOrder(String ordersn, String openid){
        Map<String, Object> jsonObject = repastService.toRestoreOrder(ordersn, openid);
        if(jsonObject!=null){
            return success("支付成功",jsonObject);
        }
        return failed("支付失败");
    }
    /**
     * 点击提交订单后，将预定单中所有信息存入订单
     * @param order
     * @return
     */
//    @ApiOperation(value = "提交订单", notes = "执行保存订单操作")
//    @PostMapping("/saveOrderInfo")
//    public ResultData savePreOrderInfo(Order order){
//        Boolean aBoolean = repastService.savePreOrderInfo(order);
//        if(true==aBoolean){
//            return success("提交成功");
//        }
//        return failed("提交失败");
//    }

    /**
     * 用户购买成功后，可查看刚下的订单
     * @param id
     * @return
     */
//    @GetMapping("/getOrderInfo")
//    @ApiOperation(value = "查看订单", notes = "获取订单信息")
//    public ResultData getOrderInfoById(@RequestParam("id") Long id){
//        Order orderInfo = repastService.getOrderInfoById(id);
//        System.out.println(orderInfo);
//        if(null!=orderInfo){
//            return success("查询成功",orderInfo);
//        }else {
//            return failed("该订单不存在");
//        }
//    }

//    /**
//     * 提交订单，需要获取购物车中用户选择所有的商品信息
//     * @param memberId
//     * @return
//     */
//    @GetMapping("/getCartAllProductByPrimary")
//    @ApiOperation(value = "查询购物车商品", notes = "获取购物车商品信息")
//    public ResultData getCartAllProductByPrimary(Long memberId){
//        return null;
//    }

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
