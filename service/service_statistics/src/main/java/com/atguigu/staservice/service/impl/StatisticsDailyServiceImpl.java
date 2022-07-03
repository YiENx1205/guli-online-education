package com.atguigu.staservice.service.impl;

import com.atguigu.commonutils.R;
import com.atguigu.servicebase.handler.exception.CustomException;
import com.atguigu.staservice.client.UCenterMemberClient;
import com.atguigu.staservice.entity.StatisticsDaily;
import com.atguigu.staservice.mapper.StatisticsDailyMapper;
import com.atguigu.staservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author Quan
 * @since 2021-08-10
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {
    @Resource
    private UCenterMemberClient memberClient;

    @Override
    public Integer countRegisterDay(String day) {
        // 如果有相同日期的数据就先删除
        QueryWrapper<StatisticsDaily> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date_calculated", day);
        this.remove(queryWrapper);

        Integer countRegisterDay = memberClient.countRegisterDay(day);

        StatisticsDaily sta = new StatisticsDaily();
        sta.setRegisterNum(countRegisterDay);
        sta.setDateCalculated(day);

        sta.setCourseNum(1);
        sta.setLoginNum(1);
        sta.setVideoViewNum(1);

        this.save(sta);

        return countRegisterDay;
    }

    @Override
    public Map<String, Object> showData(String type, String begin, String end) throws Exception {
        QueryWrapper<StatisticsDaily> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("date_calculated", begin, end);
        queryWrapper.select("date_calculated", type);
        queryWrapper.orderByAsc("date_calculated");

        List<StatisticsDaily> statisticsDailies = baseMapper.selectList(queryWrapper);
        // 日期集合
        ArrayList<String> dateList = new ArrayList<>();
        // 统计值集合
        ArrayList<Integer> countList = new ArrayList<>();
        String typeMethod;
        switch (type) {
            case "login_num":
                typeMethod = "getLoginNum";
                break;
            case "register_num":
                typeMethod = "getRegisterNum";
                break;
            case "video_view_num":
                typeMethod = "getVideoViewNum";
                break;
            case "course_num":
                typeMethod = "getCourseNum";
                break;
            default:
                throw new CustomException(20001, "统计类型错误");
        }
        for (StatisticsDaily statisticsDaily : statisticsDailies) {
            String dateCalculated = statisticsDaily.getDateCalculated();
            dateList.add(dateCalculated);
            // 通过反射获取类型值
            Class<?> classSta = Class.forName("com.atguigu.staservice.entity.StatisticsDaily");
            Method method = classSta.getDeclaredMethod(typeMethod);
            Integer countData = (Integer) method.invoke(statisticsDaily);
            countList.add(countData);
        }
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("countList", countList);
        dataMap.put("dateList", dateList);
        return dataMap;
    }
}
