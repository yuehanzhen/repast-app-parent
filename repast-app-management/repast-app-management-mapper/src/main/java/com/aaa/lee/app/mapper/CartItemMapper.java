package com.aaa.lee.app.mapper;

import com.aaa.lee.app.domain.CartItem;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CartItemMapper extends Mapper<CartItem> {
    /**
     * 通过用户id获取购物车信息列表
     * @param memberId
     * @return
     */
    List<CartItem> getCartProductByMemberId(Long memberId);

}