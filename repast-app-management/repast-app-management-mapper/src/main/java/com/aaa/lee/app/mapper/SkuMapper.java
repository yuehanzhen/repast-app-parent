package com.aaa.lee.app.mapper;

import com.aaa.lee.app.domain.Sku;
import tk.mybatis.mapper.common.Mapper;

public interface SkuMapper extends Mapper<Sku> {
    Sku selectSkuById(Long productId);
    Integer updateStockByProductId(Sku sku);
}