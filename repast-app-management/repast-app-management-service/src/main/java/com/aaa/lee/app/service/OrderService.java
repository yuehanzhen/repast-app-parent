package com.aaa.lee.app.service;

import com.aaa.lee.app.base.BaseService;
import com.aaa.lee.app.domain.*;
import com.aaa.lee.app.mapper.OrderItemMapper;
import com.aaa.lee.app.mapper.OrderMapper;
import com.aaa.lee.app.mapper.OrderSettingMapper;
import com.aaa.lee.app.mapper.ProductMapper;
import com.aaa.lee.app.utils.*;
import com.aaa.lee.app.utils.delay.DelayCancelOrderTask;
import com.aaa.lee.app.utils.delay.DelayCancelOrderTaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.aaa.lee.app.staticstatus.StaticProperties.REDIS_KEY;

/**
 * @Author:DongMengKe
 * @Date:2019/11/21 002118:57
 * @Version 1.0
 */
@Component("OrderService")
@Scope("prototype")
public class OrderService extends BaseService<Order> {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private OrderSettingMapper orderSettingMapper;
    @Override
    public Mapper<Order> getMapper() {
        return orderMapper;
    }


    /**
     * 提交订单，保存信息
     * @param addressService
     * @param redisService
     * @param cartService
     * @param productService
     * @return
     */
    public Map<String, Object> submitOrder(AddressService addressService, RedisService redisService, CartService cartService, ProductService productService){
        Order order = new Order();
        Map map = new HashMap();
        String orderId = IDUtil.getUUID();
        //orderId = orderId + System.currentTimeMillis();// 将毫秒时间戳拼接到外部订单号
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMDDHHmmss");
       //orderId = orderId + sdf.format(new Date());// 将时间字符串拼接到外部订单号
        String userINfo = redisService.get(REDIS_KEY);
        Member member = JSONUtil.toObject(userINfo, Member.class);
        Address defaultAddress = addressService.getDefaultAddress(redisService);
        order.setOrderSn(orderId);
        order.setMemberId(member.getId());
        order.setMemberUsername(member.getUsername());
        order.setCreateTime(null);
        order.setReceiverName(defaultAddress.getName());
        order.setReceiverPostCode(defaultAddress.getPostCode());
        order.setReceiverCity(defaultAddress.getCity());
        order.setReceiverProvince(defaultAddress.getProvince());
        order.setReceiverDetailAddress(defaultAddress.getDetailAddress());
        order.setReceiverRegion(defaultAddress.getRegion());
        order.setReceiverPhone(defaultAddress.getPhoneNumber());
        String deliveryId = IDUtil.getUUID()+ System.currentTimeMillis()+sdf.format(new Date());
        order.setDeliverySn(deliveryId);
        order.setDeliveryCompany("骑手");
        order.setNote("不要辣");
        order.setConfirmStatus(0);
        order.setDeleteStatus(0);
        order.setStatus(0);
        List<CartItem> cartItemList = cartService.getCartProductByMemberId(member.getId());
        List<OrderItem> orderItems = new ArrayList<>();
        Integer giftIntergrant=0;
        Map<String, Object> productDetail=null;
        OrderSetting shopOrderSettingInfo =null;
        for (CartItem cartItem:cartItemList){
                //为1时勾选购物车商品
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderSn(orderId);
                orderItem.setProductId(cartItem.getProductId());
                orderItem.setProductPic(cartItem.getProductPic());
                orderItem.setProductName(cartItem.getProductName());
                orderItem.setProductPrice(cartItem.getPrice());
                orderItem.setProductAttr(cartItem.getProductAttr());
                orderItem.setProductBrand(cartItem.getProductBrand());
                //检查库存
                Integer quantity = cartItem.getQuantity();
                try {
                    Product product = productService.get(cartItem.getProductId());
                    //赠送的积分
                    Integer giftPoint = product.getGiftPoint();

                    if(quantity<product.getStock()){
                        orderItem.setProductQuantity(quantity);
                        giftIntergrant=giftIntergrant+giftPoint;
                    }else {
                        map.put("code","400");
                        map.put("msg","库存不足");
                        return map;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                orderItem.setProductCategoryId(cartItem.getProductCategoryId());
                orderItem.setProductSkuCode(cartItem.getProductSkuCode());
                orderItem.setProductSkuId(cartItem.getProductSkuId());
            int insert = orderItemMapper.insert(orderItem);
            System.out.println(orderItem.getId());
            shopOrderSettingInfo = orderSettingMapper.getShopOrderSettingInfo(cartItem.getShopId());
            order.setShopId(cartItem.getShopId());
        }
        order.setAutoConfirmDay(shopOrderSettingInfo.getNormalOrderOvertime());
        order.setIntegration(giftIntergrant);
        BigDecimal totalAmount = getTotalAmount(cartItemList);
        order.setTotalAmount(totalAmount);
        int i = orderMapper.insert(order);
        System.out.println(order.getId());

        if(i>0){
            map.put("code","200");
            map.put("msg","提交订单成功");
//            OrderOverTimeClose.getInstance().init();
//            ExecutorService service = Executors.newFixedThreadPool(10);
//            Runnable run = new Runnable() {
//                @Override
//                public void run() {
//                    // 创建初始订单
//                    long time = System.currentTimeMillis();
//                    String currentTime = DateUtil.getTime(time);
//                    long aftertime=time+9000;
//                    String experTime = DateUtil.getTime(aftertime);
//                    OrderInfo orderInfo = new OrderInfo();
//                    orderInfo.setOrderNo(order.getId().toString());
//                    orderInfo.setStatus(order.getStatus().toString());
//                    orderInfo.setCreateTime(currentTime);
//                    orderInfo.setExpTime(experTime);
//                    System.out.println(orderInfo.getOrderNo()+"+++++++"+orderInfo.getStatus()+"+++++"+orderInfo.getCreateTime()+"++++"+orderInfo.getExpTime());
//                    //定时15分钟关闭订单
//                    OrderOverTimeClose.getInstance().orderPutQueue(orderInfo, currentTime,  experTime);
//                }
//         };
           // service.execute(run);

            //加入延时队列，等待超时取消
            DelayCancelOrderTaskManager delayCancelOrderTaskManager = DelayCancelOrderTaskManager.getInstance();
            PayTimeoutCancelOrderProcessor processor = new PayTimeoutCancelOrderProcessor(order.getId());
            delayCancelOrderTaskManager.putTaskInTimeoutTime(processor, LocalDateTime.now().plusMinutes(1));
            //重定向至微信接口
            return map;
        }else {
            map.put("code","400");
            map.put("msg","提交订单失败");
            return map;
        }

    }

    /**
     * 计算订单中订单总金额 实际支付金额 促销优化金额 优惠卷抵扣金额
     * @param CartItems
     * @return
     */
    private BigDecimal getTotalAmount(List<CartItem> CartItems) {
        BigDecimal total = new BigDecimal("0");
        for (CartItem cartItem:CartItems){
            BigDecimal price = cartItem.getPrice();
            Integer quantity = cartItem.getQuantity();
            if(quantity>0){
                for (int i=0;i<CartItems.size()-1;i++){
                    total.add(price);
                }
            }
        }
        return total;
    }

    /**
     * 进入提交订单页面的页面数据
     * @param addressService
     * @param redisService
     * @param cartService
     * @return
     */
    public Map getOrderListInfo(AddressService addressService, RedisService redisService, CartService cartService){
        String userString = redisService.get(REDIS_KEY);
        Member member = JSONUtil.toObject(userString, Member.class);
        Address defaultAddress = addressService.getDefaultAddress(redisService);
        List<OrderItem> orderItems = new ArrayList<>();
        List<CartItem> cartItemList = cartService.getCartProductByMemberId(member.getId());
        for(CartItem cartItem:cartItemList){
            if(0==cartItem.getDeleteStatus()){
                OrderItem orderItem = new OrderItem();
                orderItem.setProductPic(cartItem.getProductPic());
                orderItem.setProductName(cartItem.getProductName());
                orderItems.add(orderItem);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("cartProductList",orderItems);
        map.put("orderTotalAmount",getTotalAmount(cartItemList));
        map.put("address",defaultAddress);
        return map;
    }

    /**
     * 修改商品的库存
     * @param ordersn
     * @return
     */
    public Boolean alterSkuAndStatus(String ordersn){
        List<OrderItem> orderItemList = orderItemMapper.getProByOrderSn(ordersn);
        if(orderItemList.size()>0){
           for (OrderItem orderItem:orderItemList){
               Integer quantity = orderItem.getProductQuantity();
               Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
               Integer integer = productMapper.updateProCountByProId(orderItem.getProductId(),(product.getStock()-quantity));
               if(integer>0){
                   continue;
               }else {
                   break;
               }
           }
           //修改库存成功后，修改订单状态为已付款
            Integer integer = orderMapper.updateOrderStatusByOrderSn(ordersn,1);
           if(integer!=1){
               return false;
           }
        }else {
            return false;
        }
        return true;
    }

    /**
     * 修改订单状态为取消订单
     * 如果用户取消订单/中间执行支付操作后，则从延迟队列中去掉任务
     * @param ordersn
     * @return
     */
    public Boolean cancalOrder(String ordersn){
        Integer integer = orderMapper.updateOrderStatus(ordersn);
        if(integer>0){
            //取消预约单，则删除延时队列中的超时等待信号
            Order order = orderMapper.getOrderInfoByOrderOrderId(ordersn);
            DelayCancelOrderTaskManager delayCancelOrderTaskManager = DelayCancelOrderTaskManager.getInstance();
            PayTimeoutCancelOrderProcessor processor = new PayTimeoutCancelOrderProcessor(order.getId());
            Duration duration = Duration.between(LocalDateTime.now(), LocalDateTime.now().plusMinutes(15));
            long timeout = TimeUnit.NANOSECONDS.convert(duration.toNanos(), TimeUnit.NANOSECONDS);
            DelayCancelOrderTask<?> delayCancelOrderTask = new DelayCancelOrderTask<>(timeout, processor);
            delayCancelOrderTaskManager.removeTask(delayCancelOrderTask);
            return true;
        }else {
            return false;
        }
    }

    /**
     * 确认收货
     * @param orderSn
     * @return
     */
    public Boolean affirmReceipt(String orderSn){
        Integer result=orderMapper.recieptDateTime(orderSn);
        if(result>0){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 修改订单状态
     * @param ordersn
     * @return
     */
    public Boolean alterOrderStatus(String ordersn){
        Integer integer = orderMapper.updateOrderStatus(ordersn);
        if(integer>0){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 获取未支付的订单信息
     * @return
     */
    public List<Order> getPayNotTimeoutOrderList(){
        List<Order> orderList = orderMapper.getPayNotTimeoutOrderList();
        if(orderList.size()>0){
            return orderList;
        }else {
            return null;
        }
    }

    /**
     * 用户从退出订单后有恢复下单
     * @param ordersn
     * @return
     */
    public Map<String, Object> toRestoreOrder(String ordersn, String openid, HttpServletRequest request){
        Map<String, Object> result= PayUtil.wxPay(ordersn,openid, request);
        if("OK".equals(result.get("errMsg"))){
            //取消预约单，则删除延时队列中的超时等待信号
            Order order = orderMapper.getOrderInfoByOrderOrderId(ordersn);
            DelayCancelOrderTaskManager delayCancelOrderTaskManager = DelayCancelOrderTaskManager.getInstance();
            PayTimeoutCancelOrderProcessor processor = new PayTimeoutCancelOrderProcessor(order.getId());
            Duration duration = Duration.between(LocalDateTime.now(), LocalDateTime.now().plusMinutes(15));
            long timeout = TimeUnit.NANOSECONDS.convert(duration.toNanos(), TimeUnit.NANOSECONDS);
            DelayCancelOrderTask<?> delayCancelOrderTask = new DelayCancelOrderTask<>(timeout, processor);
            delayCancelOrderTaskManager.removeTask(delayCancelOrderTask);
            Integer integer = orderMapper.recieptDateTime(ordersn);
                return result;
        }
        return null;
    }

    /**
     * 执行支付操作
     * @param openid
     * @param ordersn
     * @param amount
     * @return
     */
    public Map<String, Object> pay(String openid, String ordersn, Float amount, HttpServletRequest request){
        Map<String, Object> result= PayUtil.wxPay(ordersn,openid,request);
       if("OK".equals(result.get("errMsg"))){
           //取消预约单，则删除延时队列中的超时等待信号
           Order order = orderMapper.getOrderInfoByOrderOrderId(ordersn);
           DelayCancelOrderTaskManager delayCancelOrderTaskManager = DelayCancelOrderTaskManager.getInstance();
           PayTimeoutCancelOrderProcessor processor = new PayTimeoutCancelOrderProcessor(order.getId());
           Duration duration = Duration.between(LocalDateTime.now(), LocalDateTime.now().plusMinutes(15));
           long timeout = TimeUnit.NANOSECONDS.convert(duration.toNanos(), TimeUnit.NANOSECONDS);
           DelayCancelOrderTask<?> delayCancelOrderTask = new DelayCancelOrderTask<>(timeout, processor);
           delayCancelOrderTaskManager.removeTask(delayCancelOrderTask);
           return result;
       }
       return null;
    }

    /**
     * 通过订单编号获取订单信息
     * @param ordersn
     * @return
     */
    public Order getOrderInfoByOrderOrderId(String ordersn){
        //String or="'"+ordersn+"'";
        Order info = orderMapper.getOrderInfoByOrderOrderId(ordersn);
        if(info!=null){
            return info;
        }else {
            return null;
        }
    }





}
