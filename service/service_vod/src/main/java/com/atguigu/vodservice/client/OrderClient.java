package com.atguigu.vodservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author quan
 * @date 2021-08-10
 */
@FeignClient(value = "service-order")
public interface OrderClient {
    @GetMapping("/orderservice/order/isBuyCourse/{courseId}/{memberId}")
    boolean isBuyCourse(@PathVariable String courseId, @PathVariable String memberId);
}
