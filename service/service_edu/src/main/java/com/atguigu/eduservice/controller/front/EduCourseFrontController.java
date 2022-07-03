package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.JwtUtil;
import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.OrderClient;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.ChapterVo;
import com.atguigu.eduservice.entity.vo.CourseInfoFormVo;
import com.atguigu.eduservice.entity.vo.CourseQueryVo;
import com.atguigu.eduservice.entity.vo.CourseWebVo;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author quan
 * @date 2021-08-07
 */
@Api("网站首页课程接口")
@RestController
@RequestMapping("/eduservice/coursefront")
public class EduCourseFrontController {
    @Resource
    private EduCourseService courseService;

    @Resource
    private EduChapterService chapterService;

    @Resource
    private OrderClient orderClient;

    @PostMapping(value = "/getFrontCourseList/{page}/{limit}")
    public R pageList(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(name = "courseQuery", value = "查询对象")
            @RequestBody(required = false) CourseQueryVo courseQuery) {
        Page<EduCourse> pageParam = new Page<>(page, limit);
        Map<String, Object> map = courseService.pageListWeb(pageParam, courseQuery);
        return R.ok().data(map);
    }


    @GetMapping(value = "/getFrontCourseInfo/{courseId}")
    public R getFrontCourseInfo(@PathVariable String courseId, HttpServletRequest request) {
        // 查询课程信息和讲师信息
        CourseWebVo courseWebVo = courseService.selectInfoWebById(courseId);
        //查询当前课程的章节信息
        List<ChapterVo> chapterVideoList = chapterService.nestedList(courseId);
        // 查询是否已经购买
        boolean isBuyCourse;
        try {
            String memberId = JwtUtil.getMemberIdByJwtToken(request);
            isBuyCourse = orderClient.isBuyCourse(courseId, memberId);
        } catch (Exception e) {
            e.printStackTrace();
            isBuyCourse = false;
        }
        return R.ok().data("courseWebVo", courseWebVo).data("chapterVideoList", chapterVideoList).data("isBuy", isBuyCourse);
    }

    // 根据课程id查询课程信息
    @GetMapping("/getDto/{courseId}")
    public CourseWebVo getCourseInfoDto(@PathVariable String courseId) {
        // 查询课程信息和讲师信息
        return courseService.selectInfoWebById(courseId);
    }
}
