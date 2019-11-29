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
 * @Date Create in 2019/11/23 14:47
 * @Description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class OmsOrderVo implements Serializable{

    private Long memberId;
    private Long  shopId;

    private Long cartId;
    /**
     * 订单编号
     */
    private String orderSn;
    /**
     * 用户帐号
     */
    private String  memberUsername;
    /**
     * 提交时间
     */
    private Date createTime;
    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;
    /**
     * 应付金额（实际支付金额）
     */
    private BigDecimal payAmount;

    /**
     * 运费金额
     */
    private BigDecimal freightAmount;
    /**
     * 优惠券抵扣金额
     */
    private BigDecimal couponAmount;
    /**
     * 支付方式：0->未支付；1->支付宝；2->微信
     */
    private Integer payType;

    /**
     * 订单来源：0->PC订单；1->app订单
     */
    private Integer sourceType;
    /**
     * 订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单
     */
    private Integer status;
    /**
     * 订单类型：0->正常订单；1->秒杀订单
     */
    private Integer orderType;
    /**
     * 自动确认时间（天）
     */
    private Integer autoConfirmDay;
    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 收货人电话
     */
    private String receiverPhone;

    /**
     * 收货人邮编
     */
    private String  receiverPostCode;
    /**
     * 省份/直辖市
     */
    private String receiverCity;

    /**
     * 区
     */
    private String  receiverRegion;
    /**
     * 详细地址
     */
    private String receiverDetailAddress;
    /**
     * 订单备注
     */
    private String  note;
    /**
     * 商品的id
     */
    private Long productId;
    /**
     * 商品的名字
     */
    private String productName;
    /**
     * 商品的价格
     */
    private BigDecimal productPrice;
    /**
     * 商品的品牌
     */
    private String productBrand;
    /**
     *购买数量
     */
    private Integer productQuantity;

    private String  productPic;









}
