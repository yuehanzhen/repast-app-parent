package com.aaa.lee.app.service;

import com.aaa.lee.app.base.BaseService;
import com.aaa.lee.app.domain.Member;
import com.aaa.lee.app.domain.MemberReceiveAddress;
import com.aaa.lee.app.mapper.MemberReceiveAddressMapper;
import com.aaa.lee.app.utils.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

import static com.aaa.lee.app.staticstatus.StaticProperties.*;

/**
 * @Company AAA软件教育
 * @Author Seven Lee
 * @Date Create in 2019/11/21 10:39
 * @Description
 **/
@Service
public class MemberReceiveAddressService extends BaseService<MemberReceiveAddress> {

    @Autowired
    private MemberReceiveAddressMapper memberReceiveAddressMapper;

    @Override
    public Mapper<MemberReceiveAddress> getMapper() {
        return memberReceiveAddressMapper;
    }

    /**
     * @author Seven Lee
     * @description
     *      通用会员id查询会员的收获地址列表
     * @param [redisService]
     * @date 2019/11/21
     * @return java.util.List<com.aaa.lee.app.domain.MemberReceiveAddress>
     * @throws 
    **/
    public List<MemberReceiveAddress> getMemberReceiveAddress(RedisService redisService) {
        // 1.从redis中获取用户信息(redis就相当于session)
        String mrbString = redisService.get(REDIS_KEY);
        Member member = JSONUtil.toObject(mrbString, Member.class);
        return memberReceiveAddressMapper.selectMemberReceiveAddrrss(member.getId());
    }

}
