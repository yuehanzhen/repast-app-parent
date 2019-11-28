package com.aaa.lee.app.service;

import com.aaa.lee.app.base.BaseService;
import com.aaa.lee.app.domain.ShopInfo;
import com.aaa.lee.app.mapper.ShopInfoMapper;
import com.aaa.lee.app.vo.ShopInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Company AAA软件教育
 * @Author Seven Lee
 * @Date Create in 2019/11/21 11:00
 * @Description
 **/
@Service
public class ShopInfoService extends BaseService<ShopInfo> {

    @Autowired
    private ShopInfoMapper shopInfoMapper;

    @Override
    public Mapper<ShopInfo> getMapper() {
        return shopInfoMapper;
    }

    /**
     * @author Seven Lee
     * @description
     *      通过店铺主键查询店铺信息(包装类型)
     * @date 2019/11/21
     * @return java.util.List<com.aaa.lee.app.vo.ShopInfoVo>
     * @throws 
    **/
    public ShopInfoVo getShopInfoByShopId(Long shopId) {
        ShopInfoVo shopInfoByShopId = shopInfoMapper.getShopInfoByShopId(shopId);
        return shopInfoByShopId;
    }



}
