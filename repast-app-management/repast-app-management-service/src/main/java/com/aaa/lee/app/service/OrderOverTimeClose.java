package com.aaa.lee.app.service;

/**
 * @Author:DongMengKe
 * @Date:2019/11/24 002419:11
 * @Version 1.0
 */

import com.aaa.lee.app.mapper.OrderMapper;
import com.aaa.lee.app.utils.delay.DelayUtils;
import com.aaa.lee.app.vo.OrderInfo;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.DelayQueue;

/**
 * [使用延时队列DelayQueue实现订单超时关闭]
 * [后台守护线程不断的执行检测工作]
 * [双检查模式实现单例模式]
 */

@Service
public class OrderOverTimeClose {

    private volatile static OrderOverTimeClose oderOverTimeClose = null;

    private OrderOverTimeClose() {

    }
    /**
     * 单例模式，双检查锁模式，在并发环境下对象只被初始化一次
     */
    public static OrderOverTimeClose getInstance(){
        if(oderOverTimeClose == null ){
            synchronized(OrderOverTimeClose.class){
                oderOverTimeClose =  new OrderOverTimeClose();
            }
        }
        return oderOverTimeClose;
    }

    /**
     * 守护线程
     */
    private Thread mainThread;

    /**
     * 启动方法
     */
    public void init(){
        mainThread =  new Thread(() -> execute());
        mainThread.setDaemon(true);
        mainThread.setName("守护线程-->");
        mainThread.start();
    }

    /**
     * 创建空延时队列
     */
    private DelayQueue<OrderInfo> queue = new DelayQueue<OrderInfo>();

    /**
     * 读取延时队列，关闭超时订单
     */
    private void execute() {
        while (true) {
            try {
                if(queue.size() > 0){
                    //从队列里获取超时的订单
                    System.out.println(queue);
                    OrderInfo orderInfo = queue.take();
                    // 检查订单状态，是否已经成功，成功则将订单从队列中删除。
                    System.out.println(orderInfo.toString());
                    if (Objects.equals(orderInfo.getStatus(), 1)) {
                        System.out.println("已付款");
                    } else {
                        System.out.println(orderInfo.getOrderNo());
                        OrderMapper orderMapper = (OrderMapper) DelayUtils.getBean("OrderMapper");
                        Integer integer = orderMapper.updateOrderStatus(orderInfo.getOrderNo());
//                        System.out.println(boo);
                        if(integer>0){
                            System.out.println("修改状态成功");
                        }else {
                            System.out.println("失败");
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 插入订单到超时队列中
     */
    public void orderPutQueue(OrderInfo orderInfo, String createTime,
                              String overTime) {
        System.out.println("订单号：" + orderInfo.getOrderNo() + "，订单创建时间："
                + createTime + "，订单过期时间：" + overTime);
        queue.add(orderInfo);
    }

}
