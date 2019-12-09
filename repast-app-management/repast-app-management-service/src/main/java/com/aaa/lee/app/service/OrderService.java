package com.aaa.lee.app.service;

import com.aaa.lee.app.base.BaseService;
import com.aaa.lee.app.code.OrderCodeFactory;
import com.aaa.lee.app.domain.*;
import com.aaa.lee.app.mapper.*;
import com.aaa.lee.app.utils.*;
import com.aaa.lee.app.utils.delay.DelayCancelOrderTask;
import com.aaa.lee.app.utils.delay.DelayCancelOrderTaskManager;

import com.aaa.lee.app.vo.OmsOrderVo;
import com.aaa.lee.app.vo.OrderInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.Assert;
import tk.mybatis.mapper.common.Mapper;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.aaa.lee.app.staticstatus.StaticProperties.*;

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
    private MemberDefaultAddressMapper memberDefaultAddressMapper;
    @Autowired
    private OmsCartItemMapper omsCartItemMapper;
    @Autowired
    private OmsOrderMapper omsOrderMapper;
    @Autowired
    private OmsOrderItemMapper omsOrderItemMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private OrderSettingMapper orderSettingMapper;
    @Autowired
    private OrderReturnApplyMapper orderReturnApplyMapper;
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
            //
            // System.out.println(orderItem.getId());
            shopOrderSettingInfo = orderSettingMapper.getShopOrderSettingInfo(cartItem.getShopId());
            order.setShopId(cartItem.getShopId());
        }
        order.setAutoConfirmDay(shopOrderSettingInfo.getNormalOrderOvertime());
        order.setIntegration(giftIntergrant);
        BigDecimal totalAmount = getTotalAmount(cartItemList);
        order.setTotalAmount(totalAmount);
        int i = orderMapper.insert(order);
        //tem.out.println(order.getId());

        if(i>0){
            map.put("code","200");
            map.put("msg","提交订单成功");


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


    /**
     * 通过订单id查询订单信息
     * @param orderId
     * @return
     */
    public List<OrderReturnApply> getOrderByOrderId(Long orderId){
        List<OrderReturnApply> orderReturnApplies = orderReturnApplyMapper.selectOrderByOrderSId(orderId);
        if (orderReturnApplies.size()>0){
            //System.out.println(orderReturnApplies);
            return orderReturnApplies;
        }
        return null;
    }

    /**
     * 添加外卖退货原因
     * @return
     */
    public Integer insertReason(OrderReturnApply orderReturnApply) {
        int i = orderReturnApplyMapper.insert(orderReturnApply);
        //System.out.println(i);
        if (i > 0) {
            return i;
        }
        return 0;
    }

    /**
     * 通过订单id查询订单状态
     * @param orderId
     * @return
     */
    public OrderReturnApply getStatusByOrderId(Long orderId){
        OrderReturnApply order = orderReturnApplyMapper.getStatusByOrderId(orderId);
        if (null != order){
            return order;
        }
        return null;
    }
    /**
     * 根据是否付款去修改订单的状态码
     * @param statuId
     * @param redisService
     * @return
     */
    public Boolean deleteOrder(Long statuId,String token){
        Integer integer =null;
        Integer integer1=null;
        //假设statuid为1的时候 未付款完成，就是放弃付款。改为无效订单 5
        //如果为0，说明付款成功，状态码改为待发货 1
        // 1.从redis中获取用户信息(redis就相当于session)
        /*String mrbString = redisService.get(REDIS_KEY);
        Member member = JSONUtil.toObject(mrbString, Member.class);
        Long memberId = member.getId();*/
        Long memberId = omsOrderMapper.getMemberId(token);
        if(PAYMENT_FAILED==statuId){
            integer = omsOrderMapper.updateStatusFailed(memberId);
            //修改购物车状态码
            //omsCartItemMapper.updateCartStatus(memberId);
            if(integer>0){
                return true;
            }
            return false;
        }else if (PAYMENT_SUCESS==statuId){
            integer1 = omsOrderMapper.updateStatusFinish(memberId);
            if(integer1>0){
                return true;
            }
            return false;
        }
        return null;
    }

    /**
     *
     * @param omsOrder
     * @return
     */
    @Transactional
    public  Boolean addOrder(List<OmsOrderVo> omsOrderVos, String token,OrderCartService orderCartService){
        List<OrderInfoVo> orderInfo = orderCartService.getOrderInfo(token);
        //System.out.println("orderInfo"+orderInfo);
        Integer deleteStatus;
        long shopId = 0;
        long productId=0;
        Date createDate=null;
        String memberNickname=null;
        String productPic =null;
        BigDecimal price=null;
        String productName=null;
        int quantity = 0;
        Integer productServiceStatus = null;
        String shopName=null;
        for (OrderInfoVo orderInfoVo : orderInfo) {
            deleteStatus = orderInfoVo.getDeleteStatus();
            shopId = orderInfoVo.getShopId();
            productId = orderInfoVo.getProductId();
            memberNickname = orderInfoVo.getMemberNickname();
            productPic = orderInfoVo.getProductPic();
            price = orderInfoVo.getPrice();
            productName = orderInfoVo.getProductName();
            productServiceStatus = orderInfoVo.getProductServiceStatus();
            shopName = orderInfoVo.getShopName();
            quantity = orderInfoVo.getQuantity();
        }
        //订单表
        OmsOrder omsOrder = new OmsOrder();
        //订单中的商品
        OmsOrderItem omsOrderItem = new OmsOrderItem();
        //购物车
        OmsCartItem omsCartItem = new OmsCartItem();
        try{
            //生成code码
            Date date = new Date();
            long lTime = date.getTime();
            //code加入到orderSn属性中
            String orderCode = OrderCodeFactory.getOrderCode(lTime);
            //获取VO类型的各种参数
            Date date1 = new Date();
            String formatDate = null;
            //HH表示24小时制；
            DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatDate = dFormat.format(date1);
            SimpleDateFormat lsdStrFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date strD = null;
            try {
                //得出当前的时间 转化格式
                strD = lsdStrFormat.parse(formatDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int insert = 0;
            Long memberId =0L;
            //不全部用前台传的东西，一部分使用购物车查出来的
            for (OmsOrderVo omsOrderVo2 : omsOrderVos){
                //获取到用户id，商铺id，订单编号，提交时间，用户名，订单总金额，应付金额
                //shopId = omsOrderVo2.getShopId();
                //Long productId = omsOrderVo2.getProductId();
                //System.out.println("productId"+productId);
                //System.out.println("productId"+productId);
                //Long memberId = omsOrderVo2.getMemberId();
                //登录之后从redis中获取到用户的id        memberId
                /*String mrbString = redisService.get(REDIS_KEY);
                Member member = JSONUtil.toObject(mrbString, Member.class);
                memberId = member.getId();*/
                memberId = omsOrderMapper.getMemberId(token);
                MemberDefaultAddress memberDefaultAddress = memberDefaultAddressMapper.getMemberDefaultAddress(memberId);
                String receiverName = memberDefaultAddress.getName();
                String receiverPhone = memberDefaultAddress.getPhoneNumber();
                BigDecimal payAmount = omsOrderVo2.getPayAmount();
                BigDecimal totalAmount = omsOrderVo2.getTotalAmount();
                //Long cartId = omsOrderVo2.getCartId();
                //在这里判断库存是否够
                //Integer productQuantity1 = omsOrderVo2.getProductQuantity();
                Integer stock = omsOrderMapper.stock(productId);
                //.out.println("stock"+stock);
                //System.out.println("quantity"+quantity);
                //System.out.println("productQuantity"+quantity);
                //如果库存足够则继续向下执行，库存不够return false结束代码
                if(stock>=quantity){
                    //把数据存到订单对应的实体类中
                    omsOrder.setMemberId(memberId)
                            .setShopId(shopId)
                            //不需要ordersn  id 对应商品详情中的Orderid
                            .setOrderSn(orderCode)
                            .setCreateTime(strD)
                            .setTotalAmount(totalAmount)
                            .setPayAmount(payAmount)
                            //收货人信息从默认收货地址中查询，不从前台获取
                            .setReceiverName(receiverName)
                            .setReceiverPhone(receiverPhone)
                            //把订单状态改为待付款
                            .setStatus(0)
                            //把订单状态改为未删除  code码为0
                            .setDeleteStatus(0);
                    if(1==productServiceStatus){
                        omsOrder.setOrderStatus(0);
                    }else if (2==productServiceStatus){
                        omsOrder.setOrderStatus(4);
                    }
                    //把订单实体类加入到数据库中
                    insert = omsOrderMapper.insert(omsOrder);
                    //加入订单页之后，修改状态码为未付款 ,取消功能，直接在上边setStatus为0
                    //omsOrderMapper.updateStatusWait(memberId);
                    //System.out.println("这是订单页");
                }
            }
            for (OmsOrderVo omsOrderVo :omsOrderVos){
                //System.out.println("遍历出来的VO类型"+omsO derVo.toString());
                /**
                 获取订单中所包含的商品的信息，存入到数据库中
                 订单编号，商品id，商品图片，商品名字，销售价格，购买数量
                 */
                //1.先从VO类的参数中取出各种属性的值
                price =omsOrderVo.getProductPrice();
                //productName =omsOrderVo.getProductName();
                //Integer productQuantity = omsOrderVo.getProductQuantity();
                Long productId2 = omsOrderVo.getProductId();

                Long orderId = omsOrderMapper.selectLastInsertId();
                //System.out.println("从omsOrder表中获取的id"+orderId);
                Integer stock = omsOrderMapper.stock(productId2);
                if(stock>=quantity){
                    //2.放到订单相关的商品对应的实体类中
                    omsOrderItem.setOrderId(orderId)
                            .setProductId(productId2)
                            .setProductName(productName)
                            .setProductPrice(price)
                            .setProductQuantity(quantity);
                    //System.out.println("omsOrderItem实体类的数据"+omsOrderItem.toString());
                    //3.把订单相关的商品的实体类当做参数存入到订单相关商品数据库表中
                    //自定义mapper方法
                    omsOrderItemMapper.insert(omsOrderItem);
                    //System.out.println(1+"======="+"这是订单详情页");
                }
            }
            if(insert>0){
                Integer i = omsCartItemMapper.updateCartStatus(memberId);
                //int i = omsCartItemMapper.updateByPrimaryKey(omsCartItem);
                //System.out.println(i+"======="+"这是修改购物车的状态码");
                if(i>0){
                    //System.out.println("true");
                    return  true;
                }else {
                    return  false;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return  false;
    }
}
