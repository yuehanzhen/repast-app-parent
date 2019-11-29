package com.aaa.lee.app.service;

import com.aaa.lee.app.base.BaseService;
import com.aaa.lee.app.domain.Member;
import com.aaa.lee.app.domain.MemberDefaultAddress;
import com.aaa.lee.app.mapper.MemberDefaultAddressMapper;
import com.aaa.lee.app.utils.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.ArrayList;

import static com.aaa.lee.app.staticstatus.StaticProperties.REDIS_KEY;

@Service
public class MemberDefaultAddressService extends BaseService<MemberDefaultAddress> {

    @Autowired
    private MemberDefaultAddressMapper memberDefaultAddressMapper;
    @Override
    public Mapper<MemberDefaultAddress> getMapper() {
        return memberDefaultAddressMapper;
    }


    public MemberDefaultAddress getMemberDefaultAddress(RedisService redisService){
        ArrayList<MemberDefaultAddress> memberDefaultAddresses = new ArrayList<>();
        String mrbString = redisService.get(REDIS_KEY);
        Member member = JSONUtil.toObject(mrbString, Member.class);
        Long memberId = member.getId();
        MemberDefaultAddress memberDefaultAddress = memberDefaultAddressMapper.getMemberDefaultAddress(memberId);
        return memberDefaultAddress;
    }
}
