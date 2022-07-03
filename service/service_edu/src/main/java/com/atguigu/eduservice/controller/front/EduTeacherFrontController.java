package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author quan
 * @date 2021-08-05
 */
@Api("网站首页名师接口")
@RestController
@RequestMapping("/eduservice/teacherfront")
public class EduTeacherFrontController {
    @Resource
    private EduTeacherService teacherService;

    @Resource
    private EduCourseService courseService;

    @PostMapping("/getTeacherFrontList/{page}/{limit}")
    @ApiOperation(value = "分页讲师列表")
    public R getTeacherFrontList(@PathVariable Integer page, @PathVariable Integer limit) {
        Page<EduTeacher> teacherPage = new Page<>(page, limit);
        Map<String, Object> map = teacherService.getTeacherFrontList(teacherPage);
        return R.ok().data(map);
    }

    @GetMapping("/getTeacherFrontInfo/{id}")
    @ApiOperation(value = "讲师详情")
    public R getTeacherFrontInfo(@PathVariable String id) {
        EduTeacher teacher = teacherService.getById(id);
        List<EduCourse> courseList = courseService.getCourseByTeacherId(id);
        return R.ok().data("teacher", teacher).data("courseList", courseList);
    }
}
