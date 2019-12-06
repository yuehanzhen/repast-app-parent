package com.aaa.lee.app.fallback;

import com.aaa.lee.app.base.ResultData;
import com.aaa.lee.app.domain.*;
import com.aaa.lee.app.service.IRepastService;
import com.aaa.lee.app.vo.OmsOrderVo;
import com.aaa.lee.app.vo.OrderInfoVo;
import com.aaa.lee.app.vo.ShopInfoVo;
import com.google.zxing.WriterException;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Company AAA软件教育
 * @Author Seven Lee
 * @Date Create in 2019/11/20 11:41
 * @Description
 **/
@Component
public class RepastFallBackFactory implements FallbackFactory<IRepastService> {

    @Override
    public IRepastService create(Throwable throwable) {
        IRepastService repastService = new IRepastService() {
            @Override
            public Boolean doLogin(Member member) {
                System.out.println("测试登录熔断数据");
                return null;
            }

            @Override
            public List<MemberReceiveAddress> getMemberReceiveAddress() {
                System.out.println("测试收获地址列表熔断数据");
                return null;
            }

            @Override
            public ShopInfoVo getShopById(Long shopId) {
                System.out.println("测试店铺信息熔断数据");
                return null;
            }

            @Override
            public List<ProductCat> getCategoryByShopId(Long shopId) {
                System.out.println("测试商品类目熔断数据");
                return null;
            }

            @Override
            public List<Product> getProductByShopId(Long shopId) {
                System.out.println("测试主键查询商品熔断数据");
                return null;
            }

            @Override
            public List<SubmitOrderVO> getSubmitOrder(OmsCartItem omsCartItem) {
                System.out.println("测试中转提交订单页面熔断数据");
                return null;
            }

            @Override
            public int setOrder(List<SubmitOrderVO> submitOrderVO) {
                System.out.println("测试创建订单页面熔断数据");
                return 0;
            }

            /**
             * 通过会员id获取地址列表
             * @param memberid
             * @return
             */
            @Override
            public List<Address> getMemberAddressByMemberId(Long memberid) {
                System.out.println("测试获取用户地址列表熔断");
                return null;
            }

            /**
             * 获取会员收获地址
             * @return
             */
            @Override
            public Address getAddressByPrimaryId() {
                System.out.println("获取用户默认收货地址熔断");
                return null;
            }

            /**
             * 提交订单
             * @return
             */
            @Override
            public Map<String, Object> submitOrder() {
                System.out.println("提交订单熔断");
                return null;
            }


            /**
             * 取消订单
             * @param ordersn
             * @return
             */
            @Override
            public ResultData cancalOrder(String ordersn,String token) {
                System.out.println("取消顶单熔断");
                return null;
            }

            /**
             * 确认收货
             * @param orderSn
             * @return
             */
            @Override
            public ResultData affirmReceipt(String orderSn,String token) {
                System.out.println("测试确认收货熔断");
                return null;
            }

            /**
             * 支付接口
             * @param ordersn
             * @param openid
             * @param amount
             * @return
             */
            @Override
            public ResultData pay(String ordersn, String openid, Float amount,String token) {
                System.out.println("测试支付接口熔断");
                return null;
            }

            /**
             * 中途退出付款后，恢复付款
             * @param ordersn
             * @param openid
             * @return
             */
            @Override
            public ResultData toRestoreOrder(String ordersn, String openid,String token) {
                System.out.println("测试中途退出入款，恢复付款熔断");
                return null;
            }

            /**
             * 微信支付回调接口
             * @throws Exception
             */
            @Override
            public void wxNotify() throws Exception {

            }

            /**
             * 获取订单
             * @param orderId
             * @return
             */
            @Override
            public List<OrderItem> getOrderByOrderId(Long orderId) {

                return null;
            }

            /**
             * 添加退货原因
             * @param orderReturnApply
             * @return
             */
            @Override
            public Integer insertReason(OrderReturnApply orderReturnApply) {
                return null;
            }

            /**
             * 获取订单状态
             * @param orderId
             * @return
             */
            @Override
            public OrderReturnApply getStatusByOrderId(Long orderId) {
                return null;
            }

            @Override
            public MemberDefaultAddress getDefaultAddress() {
                System.out.println("测试获取默认收货地址");
                return null;
            }

            @Override
            public Boolean addOrder(List<OmsOrderVo> orderVo) {
                System.out.println("测试加入订单");
                return null;
            }

            @Override
            public Boolean updateOrder(Long statuID) {
                System.out.println("测试修改订单状态码");
                return null;
            }

            @Override
            public List<OrderInfoVo> getOrderInfo() {
                System.out.println("测试从购物车中得到商品信息");
                return null;
            }

            @Override
            public List<OrderCart> orderCart(Long shopId, String token) {
                System.out.println("测试查询购物车操作");
                return null;
            }

            @Override
            public Boolean addCart(Map<String, Object> data, String token) {
                System.out.println("测试添加购物车操作");
                return null;
            }

            @Override
            public Boolean clearCart(Long shopId, String token) {
                System.out.println("测试清空购物车操作");
                return null;
            }
        };
        return repastService;
    }
}
