package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.JwtUtil;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.ResultCode;
import com.atguigu.commonutils.vo.UcenterMemberVo;
import com.atguigu.eduservice.client.UcenterMemberClient;
import com.atguigu.eduservice.entity.EduComment;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.ChapterVo;
import com.atguigu.eduservice.entity.vo.CourseQueryVo;
import com.atguigu.eduservice.entity.vo.CourseWebVo;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCommentService;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author quan
 * @date 2021-08-07
 */
@Api("网站首页评论接口")
@RestController
@RequestMapping("/eduservice/commentfront")
public class EduCommentFrontController {
    @Resource
    private EduCommentService commentService;

    @Resource
    private UcenterMemberClient memberClient;

    @GetMapping("/listByCourseId/{current}/{size}")
    @ApiOperation("根据课程id获取评论分页列表")
    public R listByCourseId(@PathVariable Integer current, @PathVariable Integer size, String courseId) {
        Page<EduComment> eduCommentPage = new Page<>(current, size);
        Map<String, Object> map = commentService.listByCourseId(eduCommentPage, courseId);
        return R.ok().data(map);
    }

    @PostMapping("/add")
    @ApiOperation("添加评论")
    public R add(@RequestBody EduComment comment, HttpServletRequest request) {
        if (StringUtils.isEmpty(comment.getContent())) {
            return R.error().message("评论内容不能为空");
        }
        String memberId = JwtUtil.getMemberIdByJwtToken(request);
        // 通过token查询评论的那个人
        if (StringUtils.isEmpty(memberId)) {
            return R.error().code(ResultCode.NOT_LOGGED_IN).message("请登录");
        }
        // 通过feign调用接口查询用户信息
        UcenterMemberVo ucenterMemberVo = memberClient.getUcenterMember(memberId);
        if (ucenterMemberVo == null) {
            return R.error().code(ResultCode.NOT_LOGGED_IN).message("获取用户失败，token不合法或失效");
        }
        comment.setMemberId(memberId);
        comment.setAvatar(ucenterMemberVo.getAvatar());
        comment.setNickname(ucenterMemberVo.getNickname());
        commentService.save(comment);
        return R.ok();
    }
}
