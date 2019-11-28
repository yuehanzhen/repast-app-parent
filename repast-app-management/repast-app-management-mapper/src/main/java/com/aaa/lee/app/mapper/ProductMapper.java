package com.aaa.lee.app.mapper;

import com.aaa.lee.app.domain.Product;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface ProductMapper extends Mapper<Product> {

    List<Product> getProductByShopId(Long shopId);
    /**
     * 通过商品id获取商品详细信息
     * @param id
     * @return
     */
    Map<String,Object> getDetailByProductId(Long id);

    /**
     * 通过商品id修改商品库存
     * @param id
     * @param stock
     * @return
     */
    Integer updateProCountByProId(@Param("id") Long id, @Param("stock")Integer stock);
}