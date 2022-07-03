package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author quan
 * @date 2021-08-05
 */
@Api("网站首页接口")
@RestController
@RequestMapping("/eduservice/index")
public class EduIndexFrontController {
    @Resource
    private EduTeacherService teacherService;

    @Resource
    private EduCourseService courseService;

    @GetMapping("/list")
    @ApiOperation(value = "查询8们热门课程、4名讲师")
    public R index() {
        QueryWrapper<EduTeacher> teacherQueryWrapper = new QueryWrapper<>();
        teacherQueryWrapper.orderByDesc("id");
        teacherQueryWrapper.last("limit 8");
        List<EduTeacher> teacherList = teacherService.list(teacherQueryWrapper);

        QueryWrapper<EduCourse> courseQueryWrapper = new QueryWrapper<>();
        courseQueryWrapper.orderByDesc("id");
        courseQueryWrapper.last("limit 4");
        List<EduCourse> courseList = courseService.list(courseQueryWrapper);
        return R.ok().data("teacherList", teacherList).data("courseList", courseList);
    }
}
