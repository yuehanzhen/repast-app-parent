package com.aaa.lee.app.mapper;

import com.aaa.lee.app.domain.OrderItem;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OrderItemMapper extends Mapper<OrderItem> {
    /**
     * 通过订单编号获取订单中的商品
     * @param ordersn
     * @return
     */
    List<OrderItem> getProByOrderSn(String ordersn);
}