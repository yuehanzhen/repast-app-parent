package com.aaa.lee.app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * TODO
 *
 * @data 2019/11/22 23:07
 * @project repast-app-parent
 * @declaration:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SubmitOrderVO implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long oid;

    /**
     * 商品id
     */
    @Column(name = "product_id")
    private Long productId;
    /**
     * 用户id
     */
    @Column(name = "member_id")
    private Long memberId;

    /**
     * 店铺ID
     */
    @Column(name = "shop_id")
    private Long shopId;

    /**
     * 购买数量
     */
    private Integer quantity;

    /**
     * 添加到购物车的价格
     */
    private BigDecimal price;
    /**
     * 商品主图
     */
    @Column(name = "product_pic")
    private String productPic;

    /**
     * 商品名称
     */
    @Column(name = "product_name")
    private String productName;
    /**
     * 创建时间
     */
    @Column(name = "create_date")
    private Date createDate;

    /**
     * 修改时间
     */
    @Column(name = "modify_date")
    private Date modifyDate;

    /**
     * 是否删除
     */
    @Column(name = "delete_status")
    private Integer deleteStatus;
    /**
     * 会员等级id
     */
    @Column(name = "member_level_id")
    private Long memberLevelId;

    /**
     * 用户名
     */
    private String memberUsername;
    /**
     * 昵称
     */
    private String memberNickname;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 订单实付金额
     */
    private BigDecimal payAmount;

}
