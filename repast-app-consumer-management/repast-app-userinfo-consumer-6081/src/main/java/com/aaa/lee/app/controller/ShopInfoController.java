package com.aaa.lee.app.controller;

import com.aaa.lee.app.base.BaseController;
import com.aaa.lee.app.base.ResultData;
import com.aaa.lee.app.domain.Product;
import com.aaa.lee.app.domain.ProductCat;
import com.aaa.lee.app.service.IRepastService;
import com.aaa.lee.app.vo.ShopInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Company AAA软件教育
 * @Author Seven Lee
 * @Date Create in 2019/11/21 10:55
 * @Description
 *      商家controller
 **/
@RestController
@Api(value = "商家信息", tags = "商家信息接口")
public class ShopInfoController extends BaseController {

    @Autowired
    private IRepastService repastService;

    /**
     * @author Seven Lee
     * @description
     *      通过主键查询店铺信息
     * @param [shopId]
     * @date 2019/11/21
     * @return com.aaa.lee.app.base.ResultData
     * @throws 
    **/
    @GetMapping("/getById")
    @ApiOperation(value = "商店信息", notes = "通过主键查询商家信息")
    public ResultData getShopById(Long shopId) {
        ShopInfoVo shopInfoVo = repastService.getShopById(shopId);
        if(null != shopInfoVo) {
            return success(shopInfoVo);
        } else {
            return failed();
        }
    }

    /**
     * @author Seven Lee
     * @description
     *      通过店铺id获取类目列表
     * @param [shopId]
     * @date 2019/11/21
     * @return com.aaa.lee.app.base.ResultData
     * @throws 
    **/
    @GetMapping("/getCatByShopId")
    @ApiOperation(value = "商品类目", notes = "通过店铺主键查询商品类目列表")
    public ResultData getCategoryByShopId(Long shopId) {
        List<ProductCat> catList = repastService.getCategoryByShopId(shopId);
        if(null != catList) {
            return success(catList);
        }
        return failed();
    }

    /**
     * @author Seven Lee
     * @description
     *      通过店铺主键查询商品列表
     * @param [shopId]
     * @date 2019/11/21
     * @return java.util.List<com.aaa.lee.app.domain.Product>
     * @throws 
    **/
    @GetMapping("/getProductByShopId")
    @ApiOperation(value = "商品信息", notes = "通过店铺主键查询商品列表")
    public ResultData getProductByShopId(Long shopId) {
        List<Product> productList = repastService.getProductByShopId(shopId);
        if(null != productList) {
            return success(productList);
        } else {
            return failed();
        }
    }

}
