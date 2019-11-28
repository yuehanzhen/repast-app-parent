package com.aaa.lee.app.domain;

import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Table(name = "oms_cart_item")

@Accessors(chain = true)
public class OmsCartItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 商品id
     */
    @Column(name = "product_id")
    private Long productId;

    /**
     * 商品库存id
     */
    @Column(name = "product_sku_id")
    private Long productSkuId;

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
     * 销售属性1
     */
    private String sp1;

    /**
     * 销售属性2
     */
    private String sp2;

    /**
     * 销售属性3
     */
    private String sp3;

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
     * 商品副标题（卖点）
     */
    @Column(name = "product_sub_title")
    private String productSubTitle;

    /**
     * 商品sku条码
     */
    @Column(name = "product_sku_code")
    private String productSkuCode;

    /**
     * 会员昵称
     */
    @Column(name = "member_nickname")
    private String memberNickname;

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
     * 商品分类
     */
    @Column(name = "product_category_id")
    private Long productCategoryId;

    @Column(name = "product_brand")
    private String productBrand;

    @Column(name = "product_sn")
    private String productSn;

    /**
     * 商品销售属性:[{"key":"颜色","value":"颜色"},{"key":"容量","value":"4G"}]
     */
    @Column(name = "product_attr")
    private String productAttr;

    public OmsCartItem(Long id, Long productId, Long productSkuId, Long memberId, Long shopId, Integer quantity, BigDecimal price, String sp1, String sp2, String sp3, String productPic, String productName, String productSubTitle, String productSkuCode, String memberNickname, Date createDate, Date modifyDate, Integer deleteStatus, Long productCategoryId, String productBrand, String productSn, String productAttr) {
        this.id = id;
        this.productId = productId;
        this.productSkuId = productSkuId;
        this.memberId = memberId;
        this.shopId = shopId;
        this.quantity = quantity;
        this.price = price;
        this.sp1 = sp1;
        this.sp2 = sp2;
        this.sp3 = sp3;
        this.productPic = productPic;
        this.productName = productName;
        this.productSubTitle = productSubTitle;
        this.productSkuCode = productSkuCode;
        this.memberNickname = memberNickname;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.deleteStatus = deleteStatus;
        this.productCategoryId = productCategoryId;
        this.productBrand = productBrand;
        this.productSn = productSn;
        this.productAttr = productAttr;
    }

    public OmsCartItem() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getProductSkuId() {
        return productSkuId;
    }

    public void setProductSkuId(Long productSkuId) {
        this.productSkuId = productSkuId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSp1() {
        return sp1;
    }

    public void setSp1(String sp1) {
        this.sp1 = sp1;
    }

    public String getSp2() {
        return sp2;
    }

    public void setSp2(String sp2) {
        this.sp2 = sp2;
    }

    public String getSp3() {
        return sp3;
    }

    public void setSp3(String sp3) {
        this.sp3 = sp3;
    }

    public String getProductPic() {
        return productPic;
    }

    public void setProductPic(String productPic) {
        this.productPic = productPic;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSubTitle() {
        return productSubTitle;
    }

    public void setProductSubTitle(String productSubTitle) {
        this.productSubTitle = productSubTitle;
    }

    public String getProductSkuCode() {
        return productSkuCode;
    }

    public void setProductSkuCode(String productSkuCode) {
        this.productSkuCode = productSkuCode;
    }

    public String getMemberNickname() {
        return memberNickname;
    }

    public void setMemberNickname(String memberNickname) {
        this.memberNickname = memberNickname;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Integer getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(Integer deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public Long getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getProductSn() {
        return productSn;
    }

    public void setProductSn(String productSn) {
        this.productSn = productSn;
    }

    public String getProductAttr() {
        return productAttr;
    }

    public void setProductAttr(String productAttr) {
        this.productAttr = productAttr;
    }

    @Override
    public String toString() {
        return "OmsCartItem{" +
                "id=" + id +
                ", productId=" + productId +
                ", productSkuId=" + productSkuId +
                ", memberId=" + memberId +
                ", shopId=" + shopId +
                ", quantity=" + quantity +
                ", price=" + price +
                ", sp1='" + sp1 + '\'' +
                ", sp2='" + sp2 + '\'' +
                ", sp3='" + sp3 + '\'' +
                ", productPic='" + productPic + '\'' +
                ", productName='" + productName + '\'' +
                ", productSubTitle='" + productSubTitle + '\'' +
                ", productSkuCode='" + productSkuCode + '\'' +
                ", memberNickname='" + memberNickname + '\'' +
                ", createDate=" + createDate +
                ", modifyDate=" + modifyDate +
                ", deleteStatus=" + deleteStatus +
                ", productCategoryId=" + productCategoryId +
                ", productBrand='" + productBrand + '\'' +
                ", productSn='" + productSn + '\'' +
                ", productAttr='" + productAttr + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OmsCartItem that = (OmsCartItem) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(productId, that.productId) &&
                Objects.equals(productSkuId, that.productSkuId) &&
                Objects.equals(memberId, that.memberId) &&
                Objects.equals(shopId, that.shopId) &&
                Objects.equals(quantity, that.quantity) &&
                Objects.equals(price, that.price) &&
                Objects.equals(sp1, that.sp1) &&
                Objects.equals(sp2, that.sp2) &&
                Objects.equals(sp3, that.sp3) &&
                Objects.equals(productPic, that.productPic) &&
                Objects.equals(productName, that.productName) &&
                Objects.equals(productSubTitle, that.productSubTitle) &&
                Objects.equals(productSkuCode, that.productSkuCode) &&
                Objects.equals(memberNickname, that.memberNickname) &&
                Objects.equals(createDate, that.createDate) &&
                Objects.equals(modifyDate, that.modifyDate) &&
                Objects.equals(deleteStatus, that.deleteStatus) &&
                Objects.equals(productCategoryId, that.productCategoryId) &&
                Objects.equals(productBrand, that.productBrand) &&
                Objects.equals(productSn, that.productSn) &&
                Objects.equals(productAttr, that.productAttr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productId, productSkuId, memberId, shopId, quantity, price, sp1, sp2, sp3, productPic, productName, productSubTitle, productSkuCode, memberNickname, createDate, modifyDate, deleteStatus, productCategoryId, productBrand, productSn, productAttr);
    }
}