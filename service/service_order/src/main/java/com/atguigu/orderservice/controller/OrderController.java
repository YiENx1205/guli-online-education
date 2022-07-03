package com.atguigu.orderservice.controller;


import com.atguigu.commonutils.JwtUtil;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.ResultCode;
import com.atguigu.orderservice.entity.Order;
import com.atguigu.orderservice.service.OrderService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author Quan
 * @since 2021-08-08
 */
@Api("网站首页订单接口")
@RestController
@RequestMapping("/orderservice/order")
public class OrderController {
    @Resource
    private OrderService orderService;

    // 根据课程id和用户id创建订单，返回订单id
    @GetMapping("/createOrder/{courseId}")
    public R save(@PathVariable String courseId, HttpServletRequest request) {
        String memberId = JwtUtil.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(memberId)) {
            return R.error().code(ResultCode.NOT_LOGGED_IN);
        }
        String orderNo = orderService.saveOrder(courseId, memberId);
        return R.ok().data("orderNo", orderNo);
    }

    // 获取订单信息
    @GetMapping("/getOrderInfo/{orderNo}")
    public R getOrderInfo(@PathVariable String orderNo) {
        Order order = orderService.getOrderInfo(orderNo);
        return R.ok().data("order", order);
    }

    // 根据课程id和token查看该用户是否已经购买
    @GetMapping("/isBuyCourse/{courseId}/{memberId}")
    public boolean isBuyCourse(@PathVariable String courseId, @PathVariable String memberId) {
        if (StringUtils.isEmpty(memberId) || StringUtils.isEmpty(courseId)) {
            return false;
        }
        return orderService.isBuyCourse(courseId, memberId);
    }



}