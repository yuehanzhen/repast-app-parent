package com.aaa.lee.app.controller;

import com.aaa.lee.app.base.BaseController;
import com.aaa.lee.app.base.ResultData;
import com.aaa.lee.app.domain.Order;
import com.aaa.lee.app.domain.OrderItem;
import com.aaa.lee.app.domain.OrderReturnApply;
import com.aaa.lee.app.service.*;
import com.aaa.lee.app.vo.OmsOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:DongMengKe
 * @Date:2019/11/21 002118:53
 * @Version 1.0
 */
@RestController
public class OrderController extends BaseController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private CartService cartService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private MemberService memberService;


    /**
     * 获取订单信息
     * @param id
     * @return
     */
    @GetMapping("/getOrderInfo")
    public Order getOrderInfoById(@RequestParam("id") Long id){
        try {
            Order orderInfo = orderService.get(id);
            return orderInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存订单信息
     * @param order
     * @return
     */
    @PostMapping("/saveOrderInfo")
    public Boolean savePreOrderInfo(@RequestBody Order order){
        try {
            Integer save = orderService.save(order);
            if(save>0){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 提交订单
     * @return
     */
    @GetMapping("/submitOrder")
    public Map<String, Object> submitOrder(){
        Map<String, Object> map = orderService.submitOrder(addressService, redisService, cartService, productService);
        if (map!=null){
            return map;
        }
        return null;
    }

    /**
     * 获取预定单信息
     * @return
     */
    @GetMapping("/getpreOrderInfo")
    public Map<String, Object> getpreOrderInfo(){
        return orderService.getOrderListInfo(addressService, redisService, cartService);
    }

    /**
     * 取消订单
     * @param ordersn
     * @return
     */
    @GetMapping("/cancalOrder")
    public ResultData cancalOrder(@RequestParam("ordersn") String ordersn,@RequestParam("token") String token){
        Boolean tokenisexist = memberService.getMemberByToken(token);
        if(tokenisexist){
           Boolean result=orderService.cancalOrder(ordersn);
           if(result){
               return success("取消成功");
           }
           return failed("取消失败");
        }
        return failed("用户身份已过期");
    }

    /**
     * 确认收货
     * @param orderSn
     * @return
     */
    @GetMapping("/affirmReceipt")
    public ResultData affirmReceipt(@RequestParam("orderSn") String orderSn,@RequestParam("token") String token){
        Boolean tokenisexist = memberService.getMemberByToken(token);
        if(tokenisexist){
            Boolean result=orderService.affirmReceipt(orderSn);
            if(result){
                return success("收货成功");
            }
            return failed("收货失败");
        }
        return failed("用户身份已过期");
    }

    /**
     * 取消订单后恢复下单
     * @param ordersn
     * @param openid
     * @return
     */
    @GetMapping("/toRestoreOrder")
    public ResultData toRestoreOrder(@RequestParam("ordersn") String ordersn, @RequestParam("openid") String openid, @RequestParam("token") String token){
        Boolean tokenisexist = memberService.getMemberByToken(token);
        if(tokenisexist){
            Map<String,Object> jsonObject=orderService.toRestoreOrder(ordersn, openid, request);
            if(jsonObject!=null){
                return success("支付成功",jsonObject);
            }
            return failed("支付失败");
        }
        return failed("用户信息已过期");
    }

    /**
     * 通过订单编号查询订单信息
     * @param orderId
     * @return
     */
    @GetMapping("/getOrder")
    public List<OrderReturnApply> getOrderByOrderId(@RequestParam("orderId") Long orderId){
        return orderService.getOrderByOrderId(orderId);
    }

    /**
     * 添加外卖退款原因
     * @param orderReturnApply
     * @return
     */
    @PostMapping("/insertReason")
    public Integer insertReason(@RequestBody OrderReturnApply orderReturnApply){
        return orderService.insertReason(orderReturnApply);

    }

    /**
     * 通过订单id查询订单状态
     * @param orderId
     * @return
     */
    @GetMapping("/getStatus")
    public OrderReturnApply getStatusByOrderId(@RequestParam("orderId") Long orderId){
        return orderService.getStatusByOrderId(orderId);
    }
    /**
     * 加入订单
     * @param omsOrder
     * @return
     */
    @PostMapping("/addOrder")
    public Boolean addOrder(@RequestBody List<OmsOrderVo> omsOrder){
        Boolean aBoolean = orderService.addOrder(omsOrder,redisService);
        return aBoolean;
    }
    /**
     * 修改订单状态
     * @param statuID
     * @return
     */
    @PostMapping("deleteOrder")
    public Boolean updateOrder(@RequestParam Long statuID){
        Boolean aBoolean = orderService.deleteOrder(statuID, redisService);
        return aBoolean;
    }

}
