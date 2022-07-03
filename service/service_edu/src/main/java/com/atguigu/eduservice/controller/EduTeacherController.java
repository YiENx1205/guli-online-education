package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.EduTeacherQuery;
import com.atguigu.eduservice.service.impl.EduTeacherServiceImpl;
import com.atguigu.servicebase.handler.exception.CustomException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.xml.ws.Service;
import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author Quan
 * @since 2021-07-31
 */
@RestController
@RequestMapping("/eduservice/teacher")
public class EduTeacherController {
    @Resource
    private EduTeacherServiceImpl teacherService;

    @GetMapping("/list")
    public R list() {
        List<EduTeacher> list = teacherService.list(null);
        return R.ok().data("items", list);
    }

    @ApiOperation(value = "分页讲师列表")
    @PostMapping("/pageTeacherCondition/{page}/{limit}")
    public R pageList(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,
            @RequestBody EduTeacherQuery eduTeacherQuery) {
        Page<EduTeacher> pageParam = new Page<>(page, limit);
        teacherService.pageQuery(pageParam, eduTeacherQuery);
        List<EduTeacher> records = pageParam.getRecords();
        long total = pageParam.getTotal();
        return R.ok().data("total", total).data("rows", records);
    }


    @DeleteMapping("{id}")
    public R delete(@PathVariable("id") String id) {
        teacherService.removeById(id);
        return R.ok();
    }

    @PostMapping("/addTeacher")
    public R save(@ApiParam(name = "teacher", value = "讲师对象", required = true) @RequestBody EduTeacher eduTeacher) {
        boolean save = teacherService.save(eduTeacher);
        if (save) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @ApiOperation(value = "根据ID查询讲师")
    @GetMapping("getTeacher/{id}")
    public R getById(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable String id) {
        EduTeacher teacher = teacherService.getById(id);
        return R.ok().data("teacher", teacher);
    }

    @ApiOperation(value = "根据ID修改讲师")
    @PostMapping("/updateTeacher")
    public R updateById(@RequestBody EduTeacher eduTeacher) {
        teacherService.updateById(eduTeacher);
        return R.ok();
    }

    @GetMapping("/testError")
    public R testError() {
        throw new  CustomException(20002, "自定义异常");
    }

}

