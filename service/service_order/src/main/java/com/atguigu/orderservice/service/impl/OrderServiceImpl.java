package com.atguigu.orderservice.service.impl;

import com.atguigu.commonutils.vo.CourseWebVo;
import com.atguigu.commonutils.vo.UcenterMemberVo;
import com.atguigu.orderservice.client.EduClient;
import com.atguigu.orderservice.client.UcenterMemberClient;
import com.atguigu.orderservice.entity.Order;
import com.atguigu.orderservice.mapper.OrderMapper;
import com.atguigu.orderservice.service.OrderService;
import com.atguigu.orderservice.utils.PayTypeConstant;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author Quan
 * @since 2021-08-08
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Resource
    private EduClient eduClient;

    @Resource
    private UcenterMemberClient memberClient;

    @Override
    public String saveOrder(String courseId, String memberId) {
        // 通过课程id查询接口得到 课程名称、封面、讲师名称
        CourseWebVo courseWebVo = eduClient.getCourseInfoDto(courseId);
        // 课程名称
        String title = courseWebVo.getTitle();
        // 封面
        String cover = courseWebVo.getCover();
        // 讲师名称
        String teacherName = courseWebVo.getTeacherName();

        // 通过memberID查询接口得到 会员昵称、会员手机
        UcenterMemberVo ucenterMember = memberClient.getUcenterMember(memberId);
        // 会员昵称
        String nickname = ucenterMember.getNickname();
        // 会员手机
        String mobile = ucenterMember.getMobile();

        // 开始封装order对象
        Order order = new Order();
        order.setCourseId(courseId);
        order.setCourseTitle(title);
        order.setCourseCover(cover);
        order.setTeacherName(teacherName);
        order.setMemberId(memberId);
        order.setNickname(nickname);
        order.setMobile(mobile);
        order.setTotalFee(courseWebVo.getPrice());
        order.setStatus(PayTypeConstant.UNPAID);
        order.setOrderNo(IdWorker.getIdStr());

        this.save(order);
        return order.getOrderNo();
    }

    @Override
    public Order getOrderInfo(String orderNo) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean isBuyCourse(String courseId, String memberId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        queryWrapper.eq("member_id", memberId);
        queryWrapper.eq("status", PayTypeConstant.PAID);
        return baseMapper.selectCount(queryWrapper) >= 1;
    }
}
