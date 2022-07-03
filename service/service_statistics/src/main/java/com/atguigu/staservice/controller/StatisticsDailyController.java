package com.atguigu.staservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.staservice.client.UCenterMemberClient;
import com.atguigu.staservice.service.StatisticsDailyService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author Quan
 * @since 2021-08-10
 */
@RestController
@CrossOrigin
@RequestMapping("/staservice/sta")
public class StatisticsDailyController {
    @Resource
    private StatisticsDailyService statisticsDailyService;


    @GetMapping("/countRegisterDay/{day}")
    public R countRegisterDay(@PathVariable String day) {
        Integer count = statisticsDailyService.countRegisterDay(day);
        return R.ok().data("count", count);
    }

    @GetMapping("/showData/{type}/{begin}/{end}")
    public R showData(@PathVariable String type, @PathVariable String begin, @PathVariable String end) {
        try {
            Map<String, Object> map = statisticsDailyService.showData(type, begin, end);
            return R.ok().data(map);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }
    }
}

