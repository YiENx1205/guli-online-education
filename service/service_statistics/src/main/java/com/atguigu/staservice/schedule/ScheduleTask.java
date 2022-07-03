package com.atguigu.staservice.schedule;

import com.atguigu.commonutils.DateUtil;
import com.atguigu.staservice.service.StatisticsDailyService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author quan
 * @date 2021-08-10
 */
@Component
public class ScheduleTask {
    @Resource
    private StatisticsDailyService statisticsDailyService;

    // 每隔5秒执行
    @Scheduled(cron = "0/5 * * * * ?")
    public void task1() {
        System.out.println("====================task1执行了=============");
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void task2() {
        // 获取前一天的日期
        String yesterday = DateUtil.formatDate(DateUtil.addDays(new Date(), -1));
        statisticsDailyService.countRegisterDay(yesterday);
    }
}
