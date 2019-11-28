package com.aaa.lee.app.service;

import com.aaa.lee.app.base.BaseService;
import com.aaa.lee.app.domain.Address;
import com.aaa.lee.app.domain.Member;
import com.aaa.lee.app.mapper.AddressMapper;
import com.aaa.lee.app.utils.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

import static com.aaa.lee.app.staticstatus.StaticProperties.REDIS_KEY;

/**
 * @Author:DongMengKe
 * @Date:2019/11/21 002123:46
 * @Version 1.0
 */
@Service
public class AddressService extends BaseService<Address> {
    @Autowired
    private AddressMapper addressMapper;
    @Override
    public Mapper<Address> getMapper() {
        return addressMapper;
    }

    /**
     * 通过redis中用户信息获取用户地址列表
     * @return
     */
    public List<Address> getAllAddressbyId(Long memberid){
            List<Address> addressList = addressMapper.getAllAddressByMemberId(memberid);
            if(addressList.size()>0){
                return addressList;
            }
        return null;
    }

    /**
     * 根据用户id 查询默认地址
     * @param redisService
     * @return
     */
    public Address getDefaultAddress(RedisService redisService){
        String userString = redisService.get(REDIS_KEY);
        Member member = JSONUtil.toObject(userString, Member.class);
        if(null!=userString&&!"".equals(userString)){
            Address address = addressMapper.getDefaultAddressByPrimaryId(member.getId());
            if(null!=address&&!"".equals(address)){
                return address;
            }
        }
        return null;
    }
}
