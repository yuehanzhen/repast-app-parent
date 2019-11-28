package com.aaa.lee.app.mapper;

import com.aaa.lee.app.domain.MemberReceiveAddress;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface MemberReceiveAddressMapper extends Mapper<MemberReceiveAddress> {

    /**
     * @author Seven Lee
     * @description
     *      通用会员id查询出所有的收获地址列表
     * @param [memberId]
     * @date 2019/11/21
     * @return java.util.List<com.aaa.lee.app.domain.MemberReceiveAddress>
     * @throws 
    **/
    List<MemberReceiveAddress> selectMemberReceiveAddrrss(Long memberId);

}