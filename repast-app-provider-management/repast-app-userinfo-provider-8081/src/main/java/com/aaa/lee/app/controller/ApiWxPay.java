package com.aaa.lee.app.controller;


import com.aaa.lee.app.base.BaseController;
import com.aaa.lee.app.base.ResultData;
import com.aaa.lee.app.service.MemberService;
import com.aaa.lee.app.service.OrderItemService;
import com.aaa.lee.app.service.OrderService;
import com.aaa.lee.app.service.ProductService;
import com.aaa.lee.app.utils.PayUtil;
import com.aaa.lee.app.utils.WXConst;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;


/**
 * 微信支付接口
 */
@RestController
    public class ApiWxPay extends BaseController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private MemberService memberService;

    @GetMapping("/pay")
    public @ResponseBody
    ResultData pay(@RequestParam("ordersn")String ordersn, @RequestParam(name = "openid") String openid, @RequestParam(name = "amount") Float amount, @RequestParam(name = "token") String token){
        if(memberService.getMemberByToken(token)){
            Map<String,Object> pay = orderService.pay(openid, ordersn, amount, request);
            if(pay!=null){
                return success("支付成功",pay);
            }
            return failed("支付失败");
        }else {
            return failed("用户信息已失效");
        }
    }

    /**
     * 支付回调接口，微信支付成功后会自动调用
     * @throws Exception
     */
    @PostMapping("/wxNotify")
    public void wxNotify() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        //sb为微信返回的xml
        String notityXml = sb.toString();
        String resXml = "";
        Map map = WXPayUtil.xmlToMap(notityXml);

        String returnCode = (String) map.get("return_code");
        if ("SUCCESS".equals(returnCode)) {
            //验证签名是否正确
            Map<String, String> validParams = PayUtil.paraFilter(map);  //回调验签时需要去除sign和空值参数
            String prestr = PayUtil.createLinkString(validParams);
            //根据微信官网的介绍，此处不仅对回调的参数进行验签，还需要对返回的金额与系统订单的金额进行比对等
            if (PayUtil.verify(prestr, (String) map.get("sign"), WXConst.KEY, "utf-8")) {
                //说明用户付款成功
                //注意要判断微信支付重复回调，支付成功后微信会重复的进行回调
                String orderSn = (String) map.get("out_trade_no");
                orderService.alterSkuAndStatus(orderSn);

                Boolean aBoolean = orderService.alterSkuAndStatus(orderSn);
                //通知微信服务器已经支付成功
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            }
        } else {
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        BufferedOutputStream out = new BufferedOutputStream(
                response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }
}
