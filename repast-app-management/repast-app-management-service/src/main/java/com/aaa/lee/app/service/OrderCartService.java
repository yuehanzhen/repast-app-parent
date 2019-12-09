package com.aaa.lee.app.service;

import com.aaa.lee.app.base.BaseService;
import com.aaa.lee.app.domain.Member;
import com.aaa.lee.app.domain.OmsCartItem;
import com.aaa.lee.app.mapper.OmsCartItemMapper;
import com.aaa.lee.app.utils.JSONUtil;
import com.aaa.lee.app.vo.OrderInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.ArrayList;
import java.util.List;

import static com.aaa.lee.app.staticstatus.StaticProperties.CART_HIDE;
import static com.aaa.lee.app.staticstatus.StaticProperties.REDIS_KEY;

@Service
public class OrderCartService extends BaseService<OmsCartItem> {

    @Autowired
    private OmsCartItemMapper omsCartItemMapper;
    @Override
    public Mapper<OmsCartItem> getMapper() {
        return omsCartItemMapper;
    }

    /**
     * deleteStatus是要查出来，用来判断的，不是controller层的参数
     * ,Integer deleteStatus
     * @param memberId
     * @param deleteStatus
     * @return
     */
    public List<OrderInfoVo> getOrderInfo(String token){
        Integer deleteStatus = null;
        ArrayList<OrderInfoVo> orderInfoVos2 = new ArrayList<>();

        /*String mrbString = redisService.get(REDIS_KEY);
        Member member = JSONUtil.toObject(mrbString, Member.class);
        Long memberId = member.getId();*/
        Long memberId = omsCartItemMapper.getMemberId(token);
        int i = memberId.intValue();
        //System.out.println("service层得到的memeberId"+memberId);
        List<OrderInfoVo> orderInfos = omsCartItemMapper.getOrderInfo(memberId);
        //System.out.println(orderInfos.toString());
        for (OrderInfoVo orderInfo: orderInfos) {
            //System.out.println("OrderCartService层得到的数据"+orderInfo);
            deleteStatus = orderInfo.getDeleteStatus();
            if(CART_HIDE==deleteStatus){
                System.out.println("if里面中的orderInfo"+orderInfo);
                long shopId = orderInfo.getShopId();
                //根据店铺ID查询店铺名字
                String shopName = omsCartItemMapper.getShopName(shopId);
                orderInfo.setShopName(shopName);
                orderInfoVos2.add(orderInfo);
            }
        }
        return orderInfoVos2;
    }

}
