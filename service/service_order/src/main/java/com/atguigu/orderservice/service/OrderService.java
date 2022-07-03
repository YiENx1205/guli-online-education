package com.atguigu.orderservice.service;

import com.atguigu.orderservice.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author Quan
 * @since 2021-08-08
 */
public interface OrderService extends IService<Order> {

    String saveOrder(String courseId, String memberId);

    Order getOrderInfo(String orderNo);

    boolean isBuyCourse(String courseId, String memberId);
}
