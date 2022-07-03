package com.atguigu.orderservice.client;

import com.atguigu.commonutils.vo.CourseWebVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author quan
 * @date 2021-08-08
 */
@FeignClient(value = "service-edu")
@Component
public interface EduClient {
    // 根据课程id查询课程信息
    @GetMapping("/eduservice/coursefront/getDto/{courseId}")
    CourseWebVo getCourseInfoDto(@PathVariable("courseId") String courseId);
}
