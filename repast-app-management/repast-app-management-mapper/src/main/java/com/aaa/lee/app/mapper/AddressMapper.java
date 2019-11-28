package com.aaa.lee.app.mapper;

import com.aaa.lee.app.domain.Address;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface AddressMapper extends Mapper<Address> {
    /**
     * 获取用户地址列表
     * @param memberId
     * @return
     */
    List<Address> getAllAddressByMemberId(Long memberId);

    /**
     * 获取用户默认地址
     * @param memberId
     * @return
     */
    Address getDefaultAddressByPrimaryId(Long memberId);
}