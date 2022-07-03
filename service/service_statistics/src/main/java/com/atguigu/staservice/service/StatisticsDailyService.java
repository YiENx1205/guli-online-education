package com.atguigu.staservice.service;

import com.atguigu.staservice.entity.StatisticsDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author Quan
 * @since 2021-08-10
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {

    Integer countRegisterDay(String day);

    Map<String, Object> showData(String type, String begin, String end) throws Exception;
}
