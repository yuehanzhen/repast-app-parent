package com.aaa.lee.app.service;

import com.aaa.lee.app.base.BaseService;
import com.aaa.lee.app.base.CommonCart;
import com.aaa.lee.app.domain.*;
import com.aaa.lee.app.mapper.OrderCartMapper;
import com.aaa.lee.app.mapper.PComMapper;
import com.aaa.lee.app.mapper.ProductMapper;
import com.aaa.lee.app.mapper.SkuMapper;
import com.aaa.lee.app.utils.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author leeoneone
 * @date 2019/11/23 0023 14:09
 */
@Service
public class OrderCartServicey extends BaseService<OrderCart> {
    /**
     * 注入购物车表的mapper
     * 注入pms_sku_stock sku库存表的mapper
     * 注入商品表pms_product的mapper
     * 注入商品评价表pms_comment的mapper
     * 注入用户表的mapper
     */
    @Autowired
    private OrderCartMapper orderCartMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private PComMapper pComMapper;
    //产生异常，一但return就就会跳出for循环
    private Exception exception = new Exception("操作失败");
    @Override
    public Mapper<OrderCart> getMapper() {
        return orderCartMapper;
    }
    /**
     * 设置延时处理
     */
    private ScheduledExecutorService mScheduledExecutorService = Executors.newScheduledThreadPool(4);
    /**
     * 购物车信息的展示
     * @param shopId
     * @param token
     * @return
     */
    public List<OrderCart> orderCart(Long shopId, String token){
        Member member = checkToken(token);
        if (null != member){
            //通过用户token获取memberId
            //用户id
            Long memberId = member.getId();
            //根据店铺id和用户id查询购物车信息
            OrderCart selectCarts = new OrderCart();
            selectCarts.setMemberId(memberId)
                    .setShopId(shopId);
            List<OrderCart> orderCarts = orderCartMapper.selectCart(selectCarts);
            if (orderCarts.size() > 0) {
                return orderCarts;
            } else {
                return null;
            }
        }
            return null;
    }
    /**
     * 购物车商品的添加
     * @param data
     * @return
     */
  @Transactional
  public Boolean addCart(Map<String,Object> data, String token, CommonCart commonCart){
      //首先验证token
      Member member = checkToken(token);
      Long memberId = member.getId();
      if (null != member){
          //从前台获取商品信息
          List<Map> ListCart = JSONUtil.toList(JSONUtil.toJsonString(data.get("cart")), Map.class);
          if (ListCart.size()>0){
              try {
                  int productServiceStatus = Integer.parseInt(JSONUtil.toJsonString(data.get("productServiceStatus")));
                  //遍历循环页面传过来的数据
                  for (int i = 0; i < ListCart.size(); i++){
                      Map map = ListCart.get(i);
                      int quantity = Integer.parseInt(JSONUtil.toJsonString(map.get("quantity")));
                      long productId = Long.parseLong( JSONUtil.toJsonString(map.get("productId")));
                      long shopId = Long.parseLong(JSONUtil.toJsonString(map.get("shopId")));
                      //查询数据库信息，购物车删除状态码，商品删除状态码，以及库存表中的库存信息
                      OrderCart selectMemberCartPo = new OrderCart();
                      selectMemberCartPo.setProductId(productId)
                              .setMemberId(memberId);
                      OrderCart selectMemberCart = orderCartMapper.selectMemberCart(selectMemberCartPo);
                      //通过商品id 查询库存数量
                      Sku sku = skuMapper.selectSkuById(productId);
                      Integer stock = sku.getStock();
                      //通过商品id查询商品表
                      Product product = productMapper.selectProductById(productId);
                      Integer publishStatus = product.getPublishStatus();
                      System.out.println(publishStatus);
                      if (0 != quantity){
                          //页面发送的数据不为0，则说明用户要购买商品
                          if (null != selectMemberCart){
                              //查询数据库不为空，则说明购物车有该商品
                              if (1 == product.getPublishStatus()){
                                  //该商品为上架商品
                                  if (2==productServiceStatus){
                                      //商品服务类型为2，则说明该商品为超市商品，需要考虑库存问题
                                      if (stock >= quantity){
                                          //库存大于购买数量，可以正常购买
                                          /*1 有商品，上架，且购买数量大于0。考虑库存
                                           *2 修改商品数量，以及库存，设置定时如果8分钟不提交订单，则库存数量返回之前修改的数量
                                           *  */
                                          OrderCart updateCartQuantity = new OrderCart();
                                          updateCartQuantity.setProductId(productId)
                                                  .setMemberId(memberId)
                                                  .setQuantity(quantity)
                                                  .setModifyDate(commonCart.date());
                                          System.out.println(commonCart.date());
                                          Integer upDateCart = orderCartMapper.upDateCart(updateCartQuantity);
                                          //调用封装，实现对购物车表的修改
                                          commonCart.deleteTimeUpdate(stock,quantity,productId,token);
                                      }else {
                                          //库存小于购买数量，则不可以正常购买，需提示顾客库存不足
                                          System.out.println("库存不足");
                                          throw exception;
                                      }
                                  }else {
                                      //该商品服务类型为0,1，说明该商品为外卖或点餐，需要不考虑库存问题
                                      OrderCart updateCartQuantity = new OrderCart();
                                      updateCartQuantity.setProductId(productId)
                                              .setMemberId(memberId)
                                              .setQuantity(quantity)
                                              .setModifyDate(commonCart.date());
                                      Integer upDateCart = orderCartMapper.upDateCart(updateCartQuantity);
                                      if (upDateCart == 0) {
                                          System.out.println("修改购买数量失败");
                                          throw exception;
                                      }
                                  }
                              }else {
                                  //该商品为下架商品，删除购物车信息
                                  Integer deleteCart = orderCartMapper.deleteCart(selectMemberCartPo);
                                  if (deleteCart == 0){
                                      System.out.println("商品为下架，删除购物车信息成功");
                                      throw exception;
                                  }
                              }
                          }else {
                              //购物车没有数据，但用户却要购买商品
                              if (1==product.getPublishStatus()){
                                //该商品为上架商
                                  if (2==productServiceStatus){
                                      //该商品为商品需要考虑库存问题，新增购物车信息，库存减去相应的数量，于此同时设置定时，8分钟后不提交订单，则库存返回修改之前数据
                                      System.out.println("开启定时任务");
                                      commonCart.addCartCom(token,productId,shopId,quantity,productServiceStatus);
                                      commonCart.deleteTimeUpdate(stock,quantity,productId,token);
                                  }else {
                                      //该服务类型为01，不考虑库存
                                      String nickName = member.getNickname();
                                      OrderCart CartPro= new OrderCart();
                                      PCom pComAttr = pComMapper.selectPcomById(productId);
                                      //把获取到的属性放进购物车实体类中，把这些属性添加到购物车表中
                                      System.out.println("开启定时任务");
                                      commonCart.addCartCom(token,productId,shopId,quantity,productServiceStatus);
                                  }
                              }else {
                                  //想要购买，购物车也没有商品信息，则提示该商品已下架
                                  System.out.println("商品已下架");
                                  throw exception;
                              }
                          }
                      }else{
                          //页面发送的数据为0，说明用户对于该商品不需要
                          if (null != selectMemberCart){
                              //查询数据库，数据库有信息，则删除购物车信息
                              Integer integer = orderCartMapper.deleteCart(selectMemberCartPo);
                              if (integer == 0){
                                  System.out.println("删除购物车成功");
                                  throw exception;
                              }
                          }
                      }
                  }
              }catch (Exception e){
                  e.printStackTrace();
                  TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                  return false;
                  }
          }else{
                  System.out.println("前台发送数据为空");
                  return false;
              }
      }else {
          System.out.println("您还没登录，请请先登录");
          return false;
      }
      return true;
  }
    /**
     * 清空购物车
     * @param shopId
     * @param token
     * @return
     */
    public Boolean clearCart(Long shopId, String token, CommonCart commonCart){
             Member member = checkToken(token);
             if (null != member){ Long memberId = member.getId();
                 //通过店铺id和会员id修改删除状态码，来实现提交订单的时候，购物车不显示数据
                 //在订单完成后如果想要“再来一单”可以通过修改状态码来实现再次购买
                 OrderCart cartclear = new OrderCart();
                 cartclear.setShopId(shopId);
                 cartclear.setMemberId(memberId);
                 cartclear.setDeleteStatus(1);
                 cartclear.setModifyDate(commonCart.date());
                 Integer integer = orderCartMapper.clearCart(cartclear);
                 if (integer > 0) {
                     return true;
                 }
                 return false;
             }
        return false;
    }
 }





