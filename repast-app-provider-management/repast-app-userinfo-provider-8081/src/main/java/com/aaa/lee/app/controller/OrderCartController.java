package com.aaa.lee.app.controller;

import com.aaa.lee.app.base.CommonCart;
import com.aaa.lee.app.domain.OrderCart;
import com.aaa.lee.app.service.OrderCartServicey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author leeoneone
 * @date 2019/11/23 0023 15:36
 */
@RestController
public class OrderCartController {
    @Autowired
    private OrderCartServicey orderCartServicey;
    @Autowired
    private CommonCart commonCart;
    @GetMapping("/order/selectCartOperation")
    public List<OrderCart> orderCart(Long shopId, String token) {
      return orderCartServicey.orderCart(shopId, token);
    }

    @PostMapping("/order/addCartOperation")
   Boolean addCart(Map<String,Object> data,String token) {
      ArrayList cart = new ArrayList();
      Map cartMap1 = new HashMap();
      Map cartMap2 = new HashMap();
      /*Map cartMap3 = new HashMap();*/
      long a = 1L;
      long b = 2L;
      /*long c = 3L;*/
     cartMap1.put("productId",a);
     cartMap1.put("quantity",20);
     cartMap1.put("shopId",a);

    cartMap2.put("productId",b);
     cartMap2.put("quantity",20);
     cartMap2.put("shopId",a);

    /* cartMap2.put("productId",c);
     cartMap2.put("quantity",30);*/
     cart.add(0,cartMap1);
   cart.add(1,cartMap2);
    /* cart.add(2,cartMap3);*/
     data.put("cart",cart);
     data.put("productServiceStatus",2);
      token = "1";
      return  orderCartServicey.addCart(data, token,commonCart);
    }
    /*清空购物车*/
    @GetMapping("/order/clearCart")
    public  Boolean clearCart(Long shopId, String token, CommonCart commonCart){
        return   orderCartServicey.clearCart(shopId,token,commonCart);
    }
}
