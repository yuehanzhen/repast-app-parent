package com.aaa.lee.app.service;

import com.aaa.lee.app.base.BaseService;
import com.aaa.lee.app.domain.CartItem;
import com.aaa.lee.app.mapper.CartItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Author:DongMengKe
 * @Date:2019/11/22 002213:49
 * @Version 1.0
 */
@Service
public class CartService extends BaseService<CartItem> {
    @Autowired
    private CartItemMapper cartItemMapper;
    @Override
    public Mapper<CartItem> getMapper() {
        return cartItemMapper;
    }

    /**
     * 通过会员id查询购物车里商品的信息
     * @param memberId
     * @return
     */
    public List<CartItem> getCartProductByMemberId(long memberId){
        List<CartItem> cartItemList = cartItemMapper.getCartProductByMemberId(memberId);
        if(cartItemList.size()>0){
            return cartItemList;
        }else{
            return null;
        }
    }
}
