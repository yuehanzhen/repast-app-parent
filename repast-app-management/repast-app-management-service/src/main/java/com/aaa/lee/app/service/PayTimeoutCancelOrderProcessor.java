package com.aaa.lee.app.service;

import com.aaa.lee.app.domain.Order;
import com.aaa.lee.app.utils.delay.DelayUtils;
import org.springframework.scheduling.annotation.Async;

/**
 * @Author:DongMengKe
 * 超时取消的时间到了，这里要执行对订单的具体取消操作
 * @Date:2019/11/26 002618:39
 * @Version 1.0
 */

public class PayTimeoutCancelOrderProcessor implements Runnable {
    private Long orderid;
    public PayTimeoutCancelOrderProcessor(Long orderid){
        this.orderid = orderid;
    }
    @Override
    @Async
    public void run(){
        //因service无法注入，只能从bean工厂中拉取
        OrderService orderService = (OrderService) DelayUtils.getBean("OrderService");
        Order order = new Order();
        order.setId(orderid);
        try {
            orderService.alterOrderStatus(order.getOrderSn());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
