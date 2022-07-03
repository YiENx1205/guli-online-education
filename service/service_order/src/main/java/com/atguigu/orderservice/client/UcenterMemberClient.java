package com.atguigu.orderservice.client;

import com.atguigu.commonutils.vo.UcenterMemberVo;
import com.atguigu.orderservice.client.impl.UcenterMemberClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author quan
 * @date 2021-08-07
 */
@FeignClient(value = "service-ucenter", fallback = UcenterMemberClientImpl.class)
@Component
public interface UcenterMemberClient {
    @GetMapping(value = "/ucenterservice/member/getUcenterMember/{memberId}")
    UcenterMemberVo getUcenterMember(@PathVariable("memberId") String memberId);
}
