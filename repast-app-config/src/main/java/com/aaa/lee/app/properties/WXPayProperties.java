package com.aaa.lee.app.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @Author:DongMengKe
 * @Date:2019/11/23 002310:55
 * @Version 1.0
 */
@Component
@PropertySource("classpath:config/wxpay.properties")
@ConfigurationProperties(prefix = "wx.pay")
public class WXPayProperties {
    //appid
    private String appid;
    //商户id
    private String mch_id;
    //异步回调地址
    private String notify_url;
    //交易类型，默认为Native
    private String trade_type;
    //微信支付密钥
    private String key;
    //同一下单接口URL
    private String wxpay_url;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getWxpay_url() {
        return wxpay_url;
    }

    public void setWxpay_url(String wxpay_url) {
        this.wxpay_url = wxpay_url;
    }
}
