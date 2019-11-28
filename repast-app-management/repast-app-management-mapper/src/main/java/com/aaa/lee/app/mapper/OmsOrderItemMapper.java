package com.aaa.lee.app.mapper;

import com.aaa.lee.app.domain.OmsOrderItem;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OmsOrderItemMapper extends Mapper<OmsOrderItem> {
    int setOrderItem(OmsOrderItem orderItem);
}