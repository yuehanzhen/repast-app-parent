package com.aaa.lee.app.mapper;

import com.aaa.lee.app.domain.OmsOrder;
import tk.mybatis.mapper.common.Mapper;

public interface OmsOrderMapper extends Mapper<OmsOrder> {
    int setOmsOrder(OmsOrder omsOrder);
}