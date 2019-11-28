package com.aaa.lee.app.service;

import com.aaa.lee.app.domain.*;
import com.aaa.lee.app.fallback.RepastFallBackFactory;
import com.aaa.lee.app.vo.ShopInfoVo;
import com.google.zxing.WriterException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Company AAA软件教育
 * @Author Seven Lee
 * @Date Create in 2019/11/20 11:40
 * @Description
 *      当使用feign进行传参的时候，如果是对象,包装类型,实体类...必须要使用@RequestBody，并且这个@RequestBody只能在该方法中出现一次
 *          ResultData selectUsersCondition(@RequestBody User user, @RequestBody UserInfo userInfo);---->错误
 *      当传递的参数是简单类型(String, Integer....8种基本类型+String)，必须要使用@RequestParam("")，这个@RequestPara注解可以出现多个
 *          ResultData selectUsersCondition(@RquestPara("username") String username, @RequestParam("age") Integer age);---->正确
 *
 **/
@FeignClient(value = "userinfo-interface-provider", fallbackFactory = RepastFallBackFactory.class)
public interface IRepastService {

    /**
     * @author Seven Lee
     * @description
     *      执行登录操作
     * @param [member]
     * @date 2019/11/21
     * @return java.lang.Boolean
     * @throws 
    **/
    @PostMapping("/login")
    Boolean doLogin(@RequestBody Member member);

    /**
     * @author Seven Lee
     * @description
     *      根据会员id获取会员收获地址列表
     * @param []
     * @date 2019/11/21
     * @return java.util.List<com.aaa.lee.app.domain.MemberReceiveAddress>
     * @throws 
    **/
    @GetMapping("/receive")
    List<MemberReceiveAddress> getMemberReceiveAddress();

    /**
     * @author Seven Lee
     * @description
     *      通过主键查询店铺信息
     * @param [shopId]
     * @date 2019/11/21
     * @return java.lang.String
     * @throws 
    **/
    @GetMapping("/getById")
    ShopInfoVo getShopById(@RequestParam("shopId") Long shopId);

    /**
     * @author Seven Lee
     * @description
     *      通过店铺主键查询商品类目列表
     * @param [shopId]
     * @date 2019/11/21
     * @return com.aaa.lee.app.base.ResultData
     * @throws 
    **/
    @GetMapping("/getCatByShopId")
    List<ProductCat> getCategoryByShopId(Long shopId);

    /**
     * @author Seven Lee
     * @description
     *      通过店铺主键查询商品列表
     * @param [shopId]
     * @date 2019/11/21
     * @return java.util.List<com.aaa.lee.app.domain.Product>
     * @throws 
    **/
    @GetMapping("/getProductByShopId")
    List<Product> getProductByShopId(Long shopId);

    /**
     * 通过店铺id和用户id获取当前用户所选购当前商家的商品
     * @param shopId
     * @param memberId
     * @return
     */
    @PostMapping("/getSubmitOrder")
    List<SubmitOrderVO> getSubmitOrder(@RequestBody OmsCartItem omsCartItem);

    /**
     * 通过页面信息创建订单
     */
    @PostMapping("/setOrder")
    int setOrder(@RequestBody List<SubmitOrderVO>  submitOrderVO);
    /**
     * 获取用户订单的收货地址
     * @return
     */
    @GetMapping("/getMemberAddressByMemberId")
    List<Address> getMemberAddressByMemberId(@RequestParam("memberid") Long memberid);
    /**
     * 通过主键id查询用户默认收货地址
     * @return
     */
    @GetMapping("/getAddressByPrimaryId")
    Address getAddressByPrimaryId();

    /**
     * 提交订单信息
     * @return
     */
    @GetMapping("/submitOrder")
    Map<String, Object> submitOrder();

    /**
     * 生成二维码
     * @param ordersn
     * @param payamout
     * @throws WriterException
     * @throws IOException
     */
    @GetMapping("/api/wxpay")
    boolean generateCode(@RequestParam(value = "order_sn") String ordersn, @RequestParam(value = "pay_amount") BigDecimal payamout)throws WriterException, IOException ;

    /**
     * 调用微信支付接口
     * @param openid
     * @return
     */
    @RequestMapping(value = "/api/wxPay", method = RequestMethod.GET)
    Map<String, Object> wxPay(@RequestParam("openid") String openid);

    /**
     * 取消订单
     * @param ordersn
     * @return
     */
    @GetMapping("/cancalOrder")
    Boolean cancalOrder(@RequestParam("ordersn") String ordersn);

    /**
     * 确认收货
     * @param orderSn
     * @return
     */
    @GetMapping("/affirmReceipt")
    Boolean affirmReceipt(@RequestParam("orderSn") String orderSn);

    /**
     * 微信支付接口
     * @param ordersn
     * @param openid
     * @param amount
     * @return
     */
    @GetMapping("/pay")
    Map<String, Object>  pay(@RequestParam("ordersn")String ordersn, @RequestParam(name = "openid") String openid, @RequestParam(name = "amount") Float amount);

    /**
     * 恢复下单
     * @param ordersn
     * @param openid
     * @return
     */
    @GetMapping("/toRestoreOrder")
    public Map<String, Object> toRestoreOrder(@RequestParam("ordersn") String ordersn, @RequestParam("openid") String openid);

    /**
     * 微信支付的回调地址
     * @throws Exception
     */
    @PostMapping("/wxNotify")
    void wxNotify() throws Exception ;


    /**
     * 通过订单id查询订单信息
     * @param orderId
     * @return
     */
    @GetMapping("/getOrder")
    List<OrderItem> getOrderByOrderId(@RequestParam("orderId") Long orderId);

    /**
     * 增加退款原因
     * @param orderReturnApply
     * @return
     */
    @PostMapping("/insertReason")
    Integer insertReason(@RequestBody OrderReturnApply orderReturnApply);


    /**
     * 通过订单id查询订单状态
     * @param orderId
     * @return
     */
    @GetMapping("/getStatus")
    OrderReturnApply getStatusByOrderId(@RequestParam("orderId") Long orderId);


}
