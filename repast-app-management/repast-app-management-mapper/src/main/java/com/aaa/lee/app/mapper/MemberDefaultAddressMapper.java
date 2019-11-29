package com.aaa.lee.app.mapper;

import com.aaa.lee.app.domain.MemberDefaultAddress;
import tk.mybatis.mapper.common.Mapper;

public interface MemberDefaultAddressMapper extends Mapper<MemberDefaultAddress> {

    MemberDefaultAddress getMemberDefaultAddress(Long memberId);

}