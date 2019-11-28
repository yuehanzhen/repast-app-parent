package com.aaa.lee.app.utils.delay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Author: xiehao
 * @Date: 2018-08-11
 * @Description 超时取消的管理类，管理延迟队列，放出一个线程轮询监控延迟队列；有一些方法用来往延迟队列中压入task，有方法用来从队列中remove task。还有初始化方法，开始执行轮询监控线程
 */
@Component
public class DelayCancelOrderTaskManager {
   private static final Logger logger = LoggerFactory.getLogger(DelayCancelOrderTaskManager.class);
   //延迟队列，一切都是围绕它来运转的
   private DelayQueue<DelayCancelOrderTask<?>> delayQueue;
   //超时取消的管理类是个单例，因为系统启动要有方法往队列中插入延迟task，所以搞成饿汉模式
   private static DelayCancelOrderTaskManager instance = new DelayCancelOrderTaskManager();
   // 守护线程,用于轮询延时队列
   private Thread daemonThread;
   //该方法初始管理类时调用，初始化延迟队列，同时初始化轮询线程
   private DelayCancelOrderTaskManager(){
      delayQueue = new DelayQueue<DelayCancelOrderTask<?>>();
      this.init();
   }
   public static DelayCancelOrderTaskManager getInstance(){
      return instance;
   }
   //初始化轮询监控守护线程
   public void init(){
      //lambda表达式，->的意思其实是静态方法，初始化随即执行的。
      daemonThread = new Thread(()-> {
         try{
            System.out.println("daemonThread start");
            execute();
         }catch (Exception e){
            logger.error("轮询线程出错",e);
         }
      });
      daemonThread.setName("DelayQueueMonitorThread");
      daemonThread.start();
   }
   //初始化的时候开始执行
   private void execute(){
      //不断轮询
      while(true){
         //此处仅为打印日志方便
         Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
         logger.info("线程数--------------" + map.size());
         logger.info(System.currentTimeMillis()+" 队列中的个数："+delayQueue.size());
         try{
            //从队列中取出可以取出的task。延时队列有个特点，不到时间取不出来，所以能取出来的，都是到时间即将执行的。
            DelayCancelOrderTask<?> delayCancelOrderTask = delayQueue.take();
            //task不为空，则开始执行
            if(delayCancelOrderTask != null){
               //获取task里面的处理线程，该线程会丢到线程池中处理
               Runnable payTimeoutCancelOrderProcessor = delayCancelOrderTask.getProcessor();
               if(payTimeoutCancelOrderProcessor == null){
                  continue;
               }
               //线程执行
               payTimeoutCancelOrderProcessor.run();
               //执行完毕，从队列中删除task
               this.removeTask(delayCancelOrderTask);
            }
         }catch (Exception e){
            logger.error("线程执行错误：",e);
         }
      }
   }
 
   /**
    * @Author xiehao
    * @Date 2018/8/11 18:15
    * @Param
    * @Description 传入的超时时间为以秒为单位
    */
   public void putTaskInSeconds(Runnable task, long timeoutPeriod){
      long timeout = TimeUnit.NANOSECONDS.convert(timeoutPeriod, TimeUnit.SECONDS);
      DelayCancelOrderTask<?> delayCancelOrderTask = new DelayCancelOrderTask<>(timeout,task);
      delayQueue.put(delayCancelOrderTask);
   }
 
   /**
    * @Author xiehao
    * @Date 2018/8/11 18:15
    * @Param
    * @Description 传入的超时时间以为分钟为单位
    */
   public void putTaskInMinites(Runnable task, long timeoutPeriod){
      long timeout = TimeUnit.NANOSECONDS.convert(timeoutPeriod, TimeUnit.MINUTES);
      DelayCancelOrderTask<?> delayCancelOrderTask = new DelayCancelOrderTask<>(timeout,task);
      delayQueue.put(delayCancelOrderTask);
   }
   /**
    * @Author xiehao
    * @Date 2018/8/11 18:15
    * @Param
    * @Description 传入的超时时间以小时为单位
    */
   public void putTaskInHours(Runnable task, long timeoutPeriod){
      long timeout = TimeUnit.NANOSECONDS.convert(timeoutPeriod, TimeUnit.HOURS);
      DelayCancelOrderTask<?> delayCancelOrderTask = new DelayCancelOrderTask<>(timeout,task);
      delayQueue.put(delayCancelOrderTask);
   }
   /**
    * @Author xiehao
    * @Date 2018/8/11 18:15
    * @Param
    * @Description 传入的超时时间为自定义的单位
    */
   public void putTaskInOwnDefine(Runnable task, long timeoutPeriod, TimeUnit unit){
      long timeout = TimeUnit.NANOSECONDS.convert(timeoutPeriod,unit);
      DelayCancelOrderTask<?> delayCancelOrderTask = new DelayCancelOrderTask<>(timeout,task);
      delayQueue.put(delayCancelOrderTask);
   }
 
   /**
    * @Author xiehao
    * @Date 2018/8/11 18:15
    * @Param
    * @Description 传入的时间为超时时间点
    */
   public void putTaskInTimeoutTime(Runnable task, LocalDateTime timeoutTime){
      Duration duration = Duration.between(LocalDateTime.now(),timeoutTime);
      long timeout = TimeUnit.NANOSECONDS.convert(duration.toNanos(), TimeUnit.NANOSECONDS);
      DelayCancelOrderTask<?> delayCancelOrderTask = new DelayCancelOrderTask<>(timeout,task);
      delayQueue.put(delayCancelOrderTask);
   }
 
   /**
    * @Author xiehao
    * @Date 2018/8/13 15:43
    * @Param
    * @Description 从队列中删除某个task。一般在用户自己取消订单的时候执行
    */
   public boolean removeTask(DelayCancelOrderTask<? extends Runnable> task){
      return delayQueue.remove(task);
   }
 
   /**
    * @Author xiehao
    * @Date 2018/8/13 15:44
    * @Param
    * @Description 判断队列中是否含有某个task
    */
   public boolean contains(DelayCancelOrderTask<? extends Runnable> task){
      return delayQueue.contains(task);
   }
   //获取队列个数。这个方法专门给打印日志核对数据用的。一般用不着它
   public Integer getDelayQueueSize(){
      System.out.println("队列中的个数："+delayQueue.size());
      return delayQueue.size();
   }
}