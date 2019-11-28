package com.aaa.lee.app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "ums_shop_information")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ShopInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 店铺所有者ID
     */
    @Column(name = "owner_id")
    private Long ownerId;

    /**
     * 店铺名称
     */
    private String name;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String borough;

    /**
     * 地址
     */
    private String address;

    private String lng;

    private String lat;

    /**
     * 是否打烊（手动打烊标示）和营业时间一起使用
     */
    private Byte closed;

    /**
     * 字符串，如：1-5|10:00-14:00，1-5|17:00-22:00，6-7|9:00-24:00  周和时间用“|”分割，周几到周几用“-”分割，多个设置用“，”分割
     */
    @Column(name = "open_time")
    private String openTime;

    /**
     * 商家电话
     */
    private String phone;

    /**
     * 商家实景照片URL，json格式 {realimsg:['http://xxx','http://xxx']}
     */
    private String images;

    /**
     * 0：冻结状态，1：正常，2：违规关闭，3：店铺到期关闭
     */
    private Byte status;

    /**
     * 授权开始时间
     */
    @Column(name = "auth_start_time")
    private Date authStartTime;

    /**
     * 授权时长，单位是月
     */
    @Column(name = "auth_long")
    private Integer authLong;

    /**
     * 授权版本的ID
     */
    @Column(name = "template_id")
    private Integer templateId;

    /**
     * 营业执照图片链接
     */
    @Column(name = "business_license")
    private String businessLicense;

    /**
     * 食品安全许可证图片链接
     */
    @Column(name = "food_license")
    private String foodLicense;

    /**
     * 卫生许可证图片链接
     */
    @Column(name = "sanitation_license")
    private String sanitationLicense;

    /**
     * 量化考核图片链接
     */
    private String assess;

    /**
     * 合同ID
     */
    @Column(name = "contract_id")
    private Long contractId;

}