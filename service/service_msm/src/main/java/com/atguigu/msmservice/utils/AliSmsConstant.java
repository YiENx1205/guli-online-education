package com.atguigu.msmservice.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author quan
 * @date 2021-08-04
 */
@Component
public class AliSmsConstant implements InitializingBean {
    @Value("${aliyun.sms.keyid}")
    private String keyId;

    @Value("${aliyun.sms.keysecret}")
    private String keySecret;

    @Value("${aliyun.sms.templateCode}")
    private String templateCode;

    @Value("${aliyun.sms.signName}")
    private String signName;

    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    public static String SMS_TEMPLATE_CODE;
    public static String SMS_SIGN_NAME;

    @Override
    public void afterPropertiesSet() throws Exception {
        ACCESS_KEY_ID = keyId;
        ACCESS_KEY_SECRET = keySecret;
        SMS_TEMPLATE_CODE = templateCode;
        SMS_SIGN_NAME = signName;
    }
}
