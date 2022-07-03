package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.impl.VodClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author quan
 * @date 2021-08-04
 */
@FeignClient(value = "service-vod", fallback = VodClientImpl.class)
@Component
public interface VodClient {
    @DeleteMapping(value = "/vodservice/video/{id}")
    R removeVideo(@PathVariable("id") String id);
}