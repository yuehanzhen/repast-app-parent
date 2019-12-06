package com.aaa.lee.app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Table(name = "pms_sku_stock")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Sku implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shop_id")
    private Long shopId;

    @Column(name = "product_id")
    private Long productId;

    /**
     * sku编码
     */
    @Column(name = "sku_code")
    private String skuCode;

    private BigDecimal price;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 预警库存
     */
    @Column(name = "low_stock")
    private Integer lowStock;

    /**
     * 销售属性1
     */
    private String sp1;

    private String sp2;

    private String sp3;

    /**
     * 展示图片
     */
    private String pic;

    /**
     * 销量
     */
    private Integer sale;

    /**
     * 单品促销价格
     */
    @Column(name = "promotion_price")
    private BigDecimal promotionPrice;

    /**
     * 锁定库存
     */
    @Column(name = "lock_stock")
    private Integer lockStock;
}