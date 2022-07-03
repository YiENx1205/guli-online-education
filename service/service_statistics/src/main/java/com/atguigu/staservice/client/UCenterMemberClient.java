package com.atguigu.staservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author quan
 * @date 2021-08-10
 */
@FeignClient(value = "service-ucenter")
public interface UCenterMemberClient {
    @GetMapping("/ucenterservice/member/countRegisterDay/{day}")
    public Integer countRegisterDay(@PathVariable String day);
}
