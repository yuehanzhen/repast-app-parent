package com.aaa.lee.app.service;

import com.aaa.lee.app.base.BaseService;
import com.aaa.lee.app.domain.OrderSetting;
import com.aaa.lee.app.mapper.OrderSettingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Author:DongMengKe
 * @Date:2019/11/22 002215:15
 * @Version 1.0
 */
public class OrderSettingService extends BaseService<OrderSetting> {
    @Autowired
    private OrderSettingMapper orderSettingMapper;
    @Override
    public Mapper<OrderSetting> getMapper() {
        return orderSettingMapper;
    }

}
