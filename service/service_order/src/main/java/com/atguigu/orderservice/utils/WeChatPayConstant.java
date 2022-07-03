package com.atguigu.orderservice.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author quan
 * @date 2021-08-09
 */
@Component
public class WeChatPayConstant implements InitializingBean {
    @Value("${wx.pay.app_id}")
    private String appId;

    @Value("${wx.pay.partner}")
    private String partner;

    @Value("${wx.pay.partnerkey}")
    private String partnerkey;

    @Value("${wx.pay.notifyurl}")
    private String notifyurl;

    public static String APPID;
    public static String PARTNER;
    public static String PARTNERKEY;
    public static String NOTIFYURL;

    @Override
    public void afterPropertiesSet() throws Exception {
        APPID = appId;
        PARTNER = partner;
        PARTNERKEY = partnerkey;
        NOTIFYURL = notifyurl;
    }
}
