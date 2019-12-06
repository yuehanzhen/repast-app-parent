package com.aaa.lee.app.mapper;

import com.aaa.lee.app.domain.OrderCart;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OrderCartMapper extends Mapper<OrderCart> {
//用户查询购物车信息
List<OrderCart> selectCart(OrderCart selectCarts);

/*加入购物车前查询用户购物车信息*/
OrderCart selectMemberCart(OrderCart selectMemberCartPo);
/*删除购物车信息*/
Integer deleteCart(OrderCart selectMemberCartPo);

 /**
  * 修改购物车信息，包括库存
  * @param updateCartQuantity
  * @return
  */
Integer upDateCart(OrderCart updateCartQuantity);
/*新增商品到购物车*/
Integer  addCartPro(OrderCart CartPro);
/*通过用户id和店铺id清除购物车*/
Integer clearCart(OrderCart cartclear);
//查询是否已经生成订单
 //
}