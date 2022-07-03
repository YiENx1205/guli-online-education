package com.atguigu.vodservice.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author quan
 * @date 2021-08-04
 */
@Component
public class AliAccountConstant implements InitializingBean {
    @Value("${aliyun.vod.keyid}")
    private String keyId;

    @Value("${aliyun.vod.keysecret}")
    private String keySecret;

    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;

    @Override
    public void afterPropertiesSet() throws Exception {
        ACCESS_KEY_ID = keyId;
        ACCESS_KEY_SECRET = keySecret;
    }
}
