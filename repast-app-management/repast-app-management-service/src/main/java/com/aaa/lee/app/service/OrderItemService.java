package com.aaa.lee.app.service;

import com.aaa.lee.app.base.BaseService;
import com.aaa.lee.app.domain.OrderItem;
import com.aaa.lee.app.mapper.OrderItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Author:DongMengKe
 * @Date:2019/11/26 002610:42
 * @Version 1.0
 */
@Service
public class OrderItemService extends BaseService<OrderItem> {
    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    public Mapper<OrderItem> getMapper() {
        return orderItemMapper;
    }
    /**
     * 通过订单编号获取订单中的商品
     * @param ordersn
     * @return
     */
    public List<OrderItem> getOrderItemList(String ordersn){
        List<OrderItem> list=orderItemMapper.getProByOrderSn(ordersn);
        if(list.size()>0){
            return list;
        }
        return null;
    }
}
