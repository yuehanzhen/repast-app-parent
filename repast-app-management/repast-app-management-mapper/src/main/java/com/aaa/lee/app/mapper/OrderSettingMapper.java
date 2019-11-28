package com.aaa.lee.app.mapper;

import com.aaa.lee.app.domain.OrderSetting;
import tk.mybatis.mapper.common.Mapper;

public interface OrderSettingMapper extends Mapper<OrderSetting> {
    /**
     * 通过购物车id获取商家订单设置信息
     * @param shopid
     * @return
     */
    OrderSetting getShopOrderSettingInfo(Long shopid);

}