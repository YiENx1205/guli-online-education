package com.atguigu.orderservice.client.impl;

import com.atguigu.commonutils.vo.UcenterMemberVo;
import com.atguigu.orderservice.client.UcenterMemberClient;
import com.atguigu.servicebase.handler.exception.CustomException;
import org.springframework.stereotype.Component;

/**
 * @author quan
 * @date 2021-08-05
 */
@Component
public class UcenterMemberClientImpl implements UcenterMemberClient {
    @Override
    public UcenterMemberVo getUcenterMember(String memberId) {
        throw new CustomException(20001, "Feign调用失败");
    }
}
