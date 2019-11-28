package com.aaa.lee.app.mapper;

import com.aaa.lee.app.domain.OmsCartItem;
import com.aaa.lee.app.domain.SubmitOrderVO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OmsCartItemMapper extends Mapper<OmsCartItem> {
    List<SubmitOrderVO> getOmsCartltem(OmsCartItem omsCartItem);

    void updataDeleteStatus(OmsCartItem omsCartItem);

}