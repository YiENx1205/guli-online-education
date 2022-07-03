package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.CourseInfoFormVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 课程基本信息 前端控制器
 * </p>
 *
 * @author Quan
 * @since 2021-08-03
 */
@Api("课程管理")
@RestController
@RequestMapping("/eduservice/course")
public class EduCourseController {
    @Resource
    private EduCourseService eduCourseService;

    @ApiOperation(value = "课程列表")
    @GetMapping
    public R list() {
        List<EduCourse> list = eduCourseService.list(null);
        return R.ok().data("list", list);
    }

    @ApiOperation(value = "新增课程")
    @PostMapping("/addCourseInfo")
    public R saveCourseInfo(
            @ApiParam(name = "CourseInfoForm", value = "课程基本信息", required =
                    true)
            @RequestBody CourseInfoFormVo courseInfoForm) {
        EduCourse eduCourse = eduCourseService.saveCourseInfo(courseInfoForm);
        return R.ok().data("courseId", eduCourse.getId());
    }

    @ApiOperation(value = "获取课程")
    @GetMapping("/getCourseInfo/{id}")
    public R getCourseInfo(@PathVariable String id) {
        CourseInfoFormVo courseInfoFormVo = eduCourseService.getCourseInfo(id);
        return R.ok().data("courseInfoVo", courseInfoFormVo);
    }

    @ApiOperation(value = "获取发布预览课程")
    @GetMapping("/getPublishCourseInfo/{id}")
    public R getPublishCourseInfo(@PathVariable String id) {
        CoursePublishVo coursePublishVo = eduCourseService.getPublishCourseInfo(id);
        return R.ok().data("publishCourse", coursePublishVo);
    }

    @ApiOperation(value = "获取课程")
    @PostMapping("/updateCourseInfo")
    public R getCourseInfo(@RequestBody CourseInfoFormVo courseInfoForm) {
        EduCourse eduCourse = eduCourseService.updateCourseInfo(courseInfoForm);
        return R.ok().data("courseId", eduCourse.getId());
    }


    @ApiOperation(value = "根据id发布课程")
    @PostMapping("/publishCourse/{id}")
    public R publishCourseById(
            @ApiParam(name = "id", value = "课程ID", required = true)
        @PathVariable String id){
        eduCourseService.publishCourseById(id);
        return R.ok();
    }

    @ApiOperation(value = "根据id删除课程")
    @DeleteMapping("{id}")
    public R delCourseById(
            @ApiParam(name = "id", value = "课程ID", required = true)
        @PathVariable String id){
        eduCourseService.removeById(id);
        // 暂时先不管章节、小节(视频)先
        return R.ok();
    }

}

