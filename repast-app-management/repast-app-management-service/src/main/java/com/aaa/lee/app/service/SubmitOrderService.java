package com.aaa.lee.app.service;

import com.aaa.lee.app.base.BaseService;
import com.aaa.lee.app.code.OrderCodeFactory;
import com.aaa.lee.app.domain.*;
import com.aaa.lee.app.mapper.OmsCartItemMapper;
import com.aaa.lee.app.mapper.OmsOrderItemMapper;
import com.aaa.lee.app.mapper.OmsOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * TODO
 *
 * @data 2019/11/21 23:34
 * @project repast-app-parent
 * @declaration:
 */
@Service
public class SubmitOrderService extends BaseService<OmsCartItem> {

    @Autowired
    private OmsCartItemMapper omsCartItemMapper;

    @Autowired
    private OmsOrderItemMapper orderItemMapper;

    @Autowired
    private OmsOrderMapper omsOrderMapper;

    /**
     * 通过店铺id和用户id获取当前用户所选购当前商家的商品
     * @return
     */
    public List<SubmitOrderVO> getSubmitOrder(OmsCartItem omsCartItem1){
        List<SubmitOrderVO> omsCartltem = omsCartItemMapper.getOmsCartltem(omsCartItem1);
        if (omsCartltem.size() > 0){
            return omsCartltem;
        }
        return null;
    }

    /**
     * 创建订单
     * 在此页面需要获取到用户id，商铺id，订单编号，提交时间，用户名，订单总金额，应付金额，
     * status（订单状态）：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭
     * pay_type（支付方式）：0->未支付；1->支付宝；2->微信     获取支付方式是为了退款的时候原路返回
     * 同时在此页面还应该改变购物车的状态码由待清空状态 ：0
     *                          转换为清空用户不可见状态：1  （更改为1是为了顾客在次进行购买的时候还能显示这些商品）
     *
     */
    @Transactional
    public int  setOrder(List<SubmitOrderVO>  submitOrderVO){

        try {
            Date date = new Date();
            long lTime = date.getTime();
            String orderCode = OrderCodeFactory.getOrderCode(lTime);

            OmsOrderItem omsOrderItem = new OmsOrderItem();
            for (SubmitOrderVO orderVO : submitOrderVO) {
            /*
             获取订单中所包含的商品的信息，存入到数据库中
                订单编号，商品id，商品图片，商品名字，销售价格，购买数量
             */
                BigDecimal price = orderVO.getPrice();//商品价格
                String productName = orderVO.getProductName();//商品名字
                Long productId = orderVO.getProductId();//商品id
                String productPic = orderVO.getProductPic();//商品图片路径
                Integer quantity = orderVO.getQuantity();//购买数量
                omsOrderItem.setProductId(productId).setProductPic(productPic).setProductName(productName)
                        .setProductPrice(price).setProductQuantity(quantity).setOrderSn(orderCode);
                orderItemMapper.setOrderItem(omsOrderItem);
            }



            Date date1 = new Date();
            String formatDate = null;
            DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //HH表示24小时制；
            formatDate = dFormat.format(date1);
            SimpleDateFormat lsdStrFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date strD = null ;
            try {
                strD = lsdStrFormat.parse(formatDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            OmsOrder omsOrder = new OmsOrder();

            for (SubmitOrderVO orderVO1 : submitOrderVO) {
                //获取到用户id，商铺id，订单编号，提交时间，用户名，订单总金额，应付金额，
                Long memberId = orderVO1.getMemberId();
                Long shopId = orderVO1.getShopId();
                String memberUsername = orderVO1.getMemberUsername();
                BigDecimal totalAmount = orderVO1.getTotalAmount();
                BigDecimal payAmount = orderVO1.getPayAmount();
                omsOrder.setMemberId(memberId);
                omsOrder.setShopId(shopId);
                omsOrder.setOrderSn(orderCode);
                omsOrder.setCreateTime(strD);
                omsOrder.setMemberUsername(memberUsername);
                omsOrder.setTotalAmount(totalAmount);
                omsOrder.setPayAmount(payAmount);
                System.out.println(omsOrder);
                int i = omsOrderMapper.setOmsOrder(omsOrder);
                System.out.println(i+"--------------------------i的值");
                if (i > 0){
                    OmsCartItem omsCartItem = new OmsCartItem();
                    omsCartItem.setMemberId(memberId);
                    omsCartItem.setShopId(shopId);
                    omsCartItem.setDeleteStatus(1);
                    omsCartItemMapper.updataDeleteStatus(omsCartItem);
                    return i;
                }
                break;
            }
        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return 0;
    }

    /**
     * @author Seven Lee
     * @description
     *      购物车的通用mapper
     * @date 2019/11/21
     * @return java.util.List<com.aaa.lee.app.vo.ShopInfoVo>
     * @throws
     **/
    public Mapper<OmsCartItem> getMapper() {
        return omsCartItemMapper;
    }


}
