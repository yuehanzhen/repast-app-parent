package com.aaa.lee.app.mapper;

import com.aaa.lee.app.domain.Product;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ProductMapper extends Mapper<Product> {

    List<Product> getProductByShopId(Long shopId);
}