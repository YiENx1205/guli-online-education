package com.atguigu.msmservice.service;

import java.util.Map;

/**
 * @author quan
 * @date 2021-08-06
 */
public interface MsmService {
    /**
     * 发送验证码
     * @param phone 手机号
     * @param param 模板中的参数变量对象,这里只有 ${code}
     * @return 是否发送成功
     */
    boolean send(String phone, Map<String, Object> param);
}
