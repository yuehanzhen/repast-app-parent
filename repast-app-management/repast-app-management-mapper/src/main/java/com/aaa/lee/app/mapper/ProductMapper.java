package com.aaa.lee.app.mapper;

import com.aaa.lee.app.domain.Product;
import com.aaa.lee.app.vo.ProductVo;
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
    ProductVo getOrderInfoByProductId(Long productId);

/**
 * @Author LiYuan
 * @Description 
 *        订单购物车
 * @Date 2019/12/6 0006 10:45
 * @Param [shopId] 
 * @return java.util.List<com.aaa.lee.app.domain.Product>
 * @Exception 
 **/

    Product selectProductById(Long productId);
    //8分钟未开启，加入购物车修改商品表库存的数量
    Integer updateStockById(Product product);
    //8分钟后库存需要返回之前的值
    
}