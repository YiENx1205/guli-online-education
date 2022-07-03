package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.vo.SubjectNestedVo;
import com.atguigu.eduservice.service.EduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author Quan
 * @since 2021-08-02
 */
@Api("课程分类管理")
@RestController
@RequestMapping("/eduservice/subject")
public class EduSubjectController {
    @Resource
    private EduSubjectService eduSubjectService;

    //添加课程分类
    @ApiOperation(value = "Excel批量导入")
    @PostMapping("/addSubject")
    public R addSubject(MultipartFile file) {
        eduSubjectService.saveSubject(file);
        return R.ok();
    }


    @GetMapping("/getAllSubject")
    public R getAllSubject() {
        List<SubjectNestedVo> subjectNestedVoList = eduSubjectService.getNestedList();
        return R.ok().data("list", subjectNestedVoList);
    }



}

