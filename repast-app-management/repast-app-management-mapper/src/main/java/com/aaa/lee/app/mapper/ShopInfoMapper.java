package com.aaa.lee.app.mapper;

import com.aaa.lee.app.domain.ShopInfo;
import com.aaa.lee.app.vo.ShopInfoVo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ShopInfoMapper extends Mapper<ShopInfo> {

    ShopInfoVo getShopInfoByShopId(Long shopId);

}