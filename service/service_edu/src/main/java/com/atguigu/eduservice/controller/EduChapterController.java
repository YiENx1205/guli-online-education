package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.vo.ChapterVo;
import com.atguigu.eduservice.entity.vo.CourseInfoFormVo;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 课程章节表 前端控制器
 * </p>
 *
 * @author Quan
 * @since 2021-08-03
 */
@Api("课程章节管理")
@RestController
@RequestMapping("/eduservice/chapter")
public class EduChapterController {
    @Resource
    private EduChapterService eduChapterService;


    @ApiOperation(value = "新增章节")
    @PostMapping("/addChapter")
    public R saveCourseInfo(@ApiParam(name = "chapterVo", value = "章节对象", required = true)
                                @RequestBody EduChapter chapter){
        eduChapterService.save(chapter);
        return R.ok();
    }

    @ApiOperation(value = "根据ID修改章节")
    @PostMapping("/updateChapter")
    public R updateCourseInfo(@ApiParam(name = "chapterVo", value = "章节对象", required = true)
                                @RequestBody EduChapter chapter){
        eduChapterService.updateById(chapter);
        return R.ok();
    }

    @ApiOperation(value = "根据ID查询章节")
    @GetMapping("/getChapterInfo/{id}")
    public R getById(
            @ApiParam(name = "id", value = "章节ID", required = true)
            @PathVariable String id){
        EduChapter chapter = eduChapterService.getById(id);
        return R.ok().data("chapter", chapter);
    }

    @ApiOperation(value = "根据ID删除章节")
    @DeleteMapping("/{id}")
    public R delById(
            @ApiParam(name = "id", value = "章节ID", required = true)
            @PathVariable String id){
        eduChapterService.removeById(id);
        return R.ok();
    }

    @ApiOperation(value = "嵌套章节数据列表")
    @GetMapping("/getChapterVideo/{courseId}")
    public R nestedListByCourseId(@PathVariable String courseId) {
        List<ChapterVo> chapterVos = eduChapterService.nestedList(courseId);
        return R.ok().data("allChapterVideo", chapterVos);
    }
}

