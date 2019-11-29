package com.aaa.lee.app.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class OrderInfoVo implements Serializable {


    //店铺ID
    private long shopId;
    //店铺名字
    private String shopName;
    //商品名字
    private String productName;
    //购买数量
    private int quantity;
    //商品价格
    private BigDecimal price;
    //商品图片
    private String productPic;
    //用户昵称(好像用不到)
    private String memberNickname;
    //创建时间
    private Date createDate;
    //是否删除状态码
    private Integer deleteStatus;
}
