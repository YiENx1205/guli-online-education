package com.atguigu.orderservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.orderservice.service.PayLogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author Quan
 * @since 2021-08-08
 */
@RestController
@RequestMapping("/orderservice/payLog")
public class PayLogController {
    @Resource
    private PayLogService payLogService;

    @GetMapping("/createNative/{orderNo}")
    public R createNative(@PathVariable String orderNo) {
        Map<String, Object> map = payLogService.createNative(orderNo);
        return R.ok().data(map);
    }

    @GetMapping("/queryPayStatus/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo) {
        // 返回信息，包含二维码地址，还有其他需要的信息
        Map<String, String> map = payLogService.queryPayStatus(orderNo);
        if (map == null) {
            return R.error().message("支付出错了");
        }
        // 如果不为空，通过map获取订单状态;
        if ("SUCCESS".equals(map.get("trade_state"))) {
            // 支付成功，添加支付记录和更改订单状态
            payLogService.updateOrderStatus(map);
            return R.ok().message("支付成功");
        }
        return R.error().code(25000).message("支付中");
    }
}

