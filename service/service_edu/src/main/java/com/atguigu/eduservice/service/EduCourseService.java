package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.CourseInfoFormVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQueryVo;
import com.atguigu.eduservice.entity.vo.CourseWebVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程基本信息 服务类
 * </p>
 *
 * @author Quan
 * @since 2021-08-03
 */
public interface EduCourseService extends IService<EduCourse> {

    EduCourse saveCourseInfo(CourseInfoFormVo courseInfoForm);

    CourseInfoFormVo getCourseInfo(String id);

    EduCourse updateCourseInfo(CourseInfoFormVo courseInfoForm);

    CoursePublishVo getPublishCourseInfo(String id);

    void publishCourseById(String id);

    List<EduCourse> getCourseByTeacherId(String teacherId);

    Map<String, Object> pageListWeb(Page<EduCourse> pageParam, CourseQueryVo courseQuery);

    /**
     * 获取课程信息
     */
    CourseWebVo selectInfoWebById(String id);

    /**
     * 更新课程浏览数
     */
    void updatePageViewCount(String id);
}
