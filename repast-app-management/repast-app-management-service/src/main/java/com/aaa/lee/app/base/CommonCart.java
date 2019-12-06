package com.aaa.lee.app.base;

import com.aaa.lee.app.domain.*;
import com.aaa.lee.app.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author leeoneone
 * @date 2019/12/3 0003 17:20
 */
@Service
public class CommonCart {
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private PComMapper pComMapper;
    @Autowired
    private OrderCartMapper orderCartMapper;
    /**
     * 延时8分钟修改封装
     */
    public Boolean deleteTimeUpdate(Integer stock,Integer quantity,Long productId,String token){
        ScheduledExecutorService mScheduledExecutorService = Executors.newScheduledThreadPool(4);
        Member member = memberMapper.selectMemberId(token);
        Long memberId = member.getId();
            //修改商品表中的库存
            Integer finallStock = stock - quantity;
            Product product = new Product();
           product.setId(productId)
                    .setStock(finallStock);
            Integer countProduct = productMapper.updateStockById(product);
            //修改库存表中的库存
           /* Sku sku = new Sku();
            sku.setProductId(productId)
                    .setStock(finallStock);
            Integer countSku = skuMapper.updateStockByProductId(sku);*/
        if (countProduct >0){
            System.out.println("修改商品和库存表的库存成功");
            //设置定时8分钟后库存返回修改之前的数据
            mScheduledExecutorService.schedule(new Runnable() {
                @Override
                public void run() {
                    //通过购物车id查询是否生成订单
                    System.out.println("开始定时");
                    OrderCart selectMemberCartPo = new OrderCart();
                    selectMemberCartPo.setProductId(productId)
                            .setMemberId(memberId);
                    OrderCart selectMemberCart = orderCartMapper.selectMemberCart(selectMemberCartPo);
                    if (1 != selectMemberCart.getDeleteStatus()) {
                        product.setId(productId)
                                .setStock(stock);
                        Integer countBackbackSkuProduct = productMapper.updateStockById(product);
                        /*sku.setProductId(productId)
                                .setStock(stock);
                        Integer count = skuMapper.updateStockByProductId(sku);
                        System.out.println("8分钟没有生成订单，库存恢复之前的数量");*/
                        if (countBackbackSkuProduct >0){
                            System.out.println("8分钟没有生成订单，库存恢复之前的数量");
                        }else {
                            System.out.println("库存恢复之前的数量异常");
                        }
                    }
                }
            },1, TimeUnit.MINUTES);
            return true;
        }else {
            System.out.println("修改商品和库存表的库存异常");
            return false;
        }
    }
    /**
     * 新增购物车封装
     */
    public Boolean addCartCom(String token,Long productId,Long shopId,Integer quantity,Integer productServiceStatus ){
        Member member = memberMapper.selectMemberId(token);
        Long memberId = member.getId();
        String nickName = member.getNickname();
        OrderCart CartPro= new OrderCart();
        PCom pComAttr = pComMapper.selectPcomById(productId);
        System.out.println(pComAttr.getProductAttribute());
        //通过商品id查询该商品的上下架,以及添加时数据的获取都需要用到该方法
        Product product = productMapper.selectProductById(productId);
        //通过店铺id和商品id查询库存,获取新增时的字段，以及库存数量
        Sku sku = skuMapper.selectSkuById(productId);
        Integer stock = sku.getStock();
        //把获取到的属性放进购物车实体类中，把这些属性添加到购物车表中
        CartPro.setProductId(productId)
                .setProductSkuId(sku.getId())
                .setMemberId(memberId)
                .setShopId(shopId)
                .setQuantity(quantity)
                .setPrice(product.getPrice())
                .setSp1(sku.getSp1())
                .setSp2(sku.getSp2())
                .setSp3(sku.getSp3())
                .setProductPic(product.getPic())
                .setProductName(product.getName())
                .setProductSubTitle(product.getSubTitle())
                .setProductSkuCode(sku.getSkuCode())
                .setMemberNickname(nickName)
                .setCreateDate(date())
                .setDeleteStatus(0)
                .setProductCategoryId(product.getProductCategoryId())
                .setProductBrand(product.getBrandName())
                .setProductSn(product.getProductSn())
                .setProductAttr(pComAttr.getProductAttribute())
                .setProductServiceStatus(productServiceStatus);
        Integer insertCart = orderCartMapper.addCartPro(CartPro);
        if (insertCart>0){
            System.out.println("新增成功");
            return true;
        }else {
            System.out.println("新增异常");
            return false;
        }
    }
    /**
     * 时间日期的封装
     * @return
     */
    public Date date(){
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
        return strD;
    }
}
