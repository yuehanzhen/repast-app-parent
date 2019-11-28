package com.aaa.lee.app.mapper;


import com.aaa.lee.app.domain.OrderReturnApply;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OrderReturnApplyMapper extends Mapper<OrderReturnApply> {
    /**
     * 通过id修改退货图片原始名称,新名称,图片路径
     * @param
     * @return
     */
//    Integer updatePicAndFileNameAndOldFileNameById(OrderReturnApply orderReturnApply);

    /**
     * 通过订单id查询订单
     * @param orderId
     * @return
     */
    List<OrderReturnApply> selectOrderByOrderSId(Long orderId);

    OrderReturnApply findOne(Long orderId);

    OrderReturnApply getStatusByOrderId(Long orderId);

}