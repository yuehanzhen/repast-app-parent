package com.aaa.lee.app.utils;

/**
 * 微信常数类
 */
public class WXConst {

    //微信小程序appid
    public static String APPID = "wx8087d8149331d27c";
    //微信小程序appsecret
    public static String APPSECRET = "e8cb3f526ac67e41dffb8fb4201873da";
    //微信支付主体
    public static String TITLE = "test-wxpay";
    //订单号
    public static String ORDERNO = "3809069092363";
    //微信商户号
    public static String MCH_ID="1532192611";
    //微信支付的商户密钥
    public static final String KEY = "D7FF70A194598ED8D95126343D6A3B21";
    //获取微信Openid的请求地址
    public static String WX_GETOPENID_URL = "";
    //支付成功后的服务器回调url
    public static final String NOTIFY_URL="https://9ec88aa8.ngrok.io/api/wxpay/notice";
    //签名方式
    public static final String SIGNTYPE = "MD5";
    //交易类型
    public static final String TRADETYPE = "JSAPI";
    //微信统一下单接口地址
    public static final String PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    //微信退款通知地址
    public static final String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
}
