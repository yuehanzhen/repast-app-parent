package com.aaa.lee.app.mapper;

import com.aaa.lee.app.domain.Order;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OrderMapper extends Mapper<Order> {
    /**
     * 通过订单id修改订单状态为“无效订单”
     * @param ordersn
     * @return
     */
    Integer updateOrderStatus(@Param("ordersn") String ordersn);

    /**
     * 修改状态为以付款
     * @param orderSn
     * @return
     */
    Integer recieptDateTime(String orderSn);

    /**
     * 通过订单编号修改状态
     * @param ordersn
     * @param status
     * @return
     */
    Integer updateOrderStatusByOrderSn(@Param("ordersn") String ordersn, @Param("status") int status);

    /**
     * 获取未付款的订单列表
     * @return
     */
    List<Order> getPayNotTimeoutOrderList();

    /**
     * 通过订单编号获取订单信息
     * @param ordersn
     * @return
     */
    Order getOrderInfoByOrderOrderId(String ordersn);

}