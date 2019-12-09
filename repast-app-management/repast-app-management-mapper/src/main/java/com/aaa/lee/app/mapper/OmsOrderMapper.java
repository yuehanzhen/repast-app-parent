package com.aaa.lee.app.mapper;

import com.aaa.lee.app.domain.OmsOrder;
import com.aaa.lee.app.vo.OmsOrderAndShopInfoVo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OmsOrderMapper extends Mapper<OmsOrder> {
    int setOmsOrder(OmsOrder omsOrder);
    Integer updateStatusFailed(Long memberId);
    Integer updateStatusWait(Long memberId);
    Integer updateStatusFinish(Long memberId);
    Long getMemberId(String token);
    Integer stock(Long productId);
    Long selectLastInsertId();
    List<OmsOrderAndShopInfoVo> getOrderAndShopInfo(Long memberId, Integer orderStatus);

}