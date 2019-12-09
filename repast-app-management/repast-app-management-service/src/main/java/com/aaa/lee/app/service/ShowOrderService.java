package com.aaa.lee.app.service;

import com.aaa.lee.app.base.BaseService;
import com.aaa.lee.app.domain.OmsOrder;
import com.aaa.lee.app.mapper.OmsOrderMapper;
import com.aaa.lee.app.mapper.UmsMemberMapper;
import com.aaa.lee.app.vo.OmsOrderAndShopInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Service
public class ShowOrderService extends BaseService<OmsOrder> {

    @Autowired
    private OmsOrderMapper omsOrderMapper;
    @Autowired
    private UmsMemberMapper umsMemberMapper;
    @Override
    public Mapper<OmsOrder> getMapper() {
        return omsOrderMapper;
    }
    public List<OmsOrderAndShopInfoVo> showOrder(String token,Integer orderStatus){
        //System.out.println("token"+token+"orderStatus"+orderStatus);
        Long memberId = umsMemberMapper.getMemberId(token);
        //System.out.println("memberId"+memberId);
        List<OmsOrderAndShopInfoVo> orderAndShopInfo = omsOrderMapper.getOrderAndShopInfo(memberId,orderStatus);
        //System.out.println(orderAndShopInfo);
        if(null!=orderAndShopInfo){
            return orderAndShopInfo;
        }else {
            return null;
}
    }
}
