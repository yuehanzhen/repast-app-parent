package com.aaa.lee.app.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Company AAA软件教育
 * @Author Lee
 * @Date Create in 2019/11/26 15:32
 * @Description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class OmsOrderAndShopInfoVo implements Serializable {

    private  Long memberId;
    /**
     * 订单编号
     */
    private  String OrderSn;
    /**
     * 提交时间
     */
    private Date createTime;
    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;
    /**
     *
     */
    private BigDecimal payAmount;
    /**
     * 订单类型
     */
    private Integer orderType;
    /**
     * 支付方式：0->未支付；1->支付宝；2->微信
     */
    private Integer payType;
    /**
     * 店铺名字
     */
    private  String name;
    /**
     * 店铺id
     */
    private Long shopId;
    /**
     * 商家主图
     */
    private String images;
    /**
     * 商家联系方式
     */
    private String phone;
    /**
     *需要传入的值：0.外卖，1：订餐，2:拼团，3:预定，4:超市，5：积分订单
     */
    private int orderStatus;

}
