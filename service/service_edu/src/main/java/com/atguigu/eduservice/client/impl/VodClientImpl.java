package com.atguigu.eduservice.client.impl;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.VodClient;
import org.springframework.stereotype.Component;

/**
 * @author quan
 * @date 2021-08-05
 */
@Component
public class VodClientImpl implements VodClient {
    @Override
    public R removeVideo(String id) {
        return R.error().message("服务降级，请稍后再试");
    }
}
