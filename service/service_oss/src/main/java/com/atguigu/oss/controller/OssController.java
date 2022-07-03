package com.atguigu.oss.controller;

import com.atguigu.commonutils.R;
import com.atguigu.oss.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author quan
 * @date 2021-08-02
 */
@Api("阿里云文件管理")
@CrossOrigin
@RestController
@RequestMapping("/ossservice/file")
public class OssController {
    @Resource
    private OssService ossService;


    @ApiOperation(value = "文件上传")
    @PostMapping("/upload")
    public R upload(@ApiParam(name = "file", value = "文件", required = true) @RequestParam MultipartFile file) {
        String uploadUrl = ossService.upload(file, "avatar");
        return R.ok().message("文件上传成功").data("url", uploadUrl);
    }

    @ApiOperation(value = "富文本图片上传")
    @PostMapping("/imageUpload")
    public R imageUpload(@ApiParam(name = "file", value = "文件", required = true) @RequestParam MultipartFile file) {
        String uploadUrl = ossService.upload(file, "image");
        return R.ok().message("文件上传成功").data("url", uploadUrl);
    }
}
