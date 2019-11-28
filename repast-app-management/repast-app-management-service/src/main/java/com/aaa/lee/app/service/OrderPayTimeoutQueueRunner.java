package com.aaa.lee.app.service;

import com.aaa.lee.app.domain.Order;
import com.aaa.lee.app.utils.delay.DelayCancelOrderTaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: xiehao
 * @Date: 2018-08-11
 * @Description
 */
 
@Component
public class OrderPayTimeoutQueueRunner  implements CommandLineRunner {
   @Autowired
   private  OrderService orderService;

   /**
    * 系统初始化时将处于待支付状态的单据丢到延时队列中，等待执行超时任务
    * @param strings
    * @throws Exception
    */
   @Override
   public void run(String... strings) throws Exception {
         new Thread(){
            @Override
            public void run() {
               DelayCancelOrderTaskManager delayCancelOrderTaskManager = DelayCancelOrderTaskManager.getInstance();
               List<Order> OrderList = orderService.getPayNotTimeoutOrderList();
               try{
                  for(Order order : OrderList){
                     PayTimeoutCancelOrderProcessor processor = new PayTimeoutCancelOrderProcessor(order.getId());
                     delayCancelOrderTaskManager.putTaskInTimeoutTime(processor, LocalDateTime.now().plusMinutes(15));
                  }
               }catch (NullPointerException e){
                  System.out.println("没有可加载的数据");
               }
            }
         }.start();

   }
}