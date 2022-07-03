package com.atguigu.vodservice.controller;

import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.atguigu.commonutils.R;
import com.atguigu.vodservice.service.VodService;
import com.atguigu.vodservice.vo.UploadVideoResponseVo;
import com.atguigu.vodservice.vo.VideoInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author quan
 * @date 2021-08-04
 */
@Api("阿里云视频点播微服务")
@RestController
@RequestMapping("/vodservice/video")
public class VodController {
    @Resource
    private VodService vodService;

    @PostMapping("/upload")
    public R uploadVideo(
            @ApiParam(name = "file", value = "文件", required = true)
            @RequestParam("file") MultipartFile file) throws Exception {
        String videoId = vodService.uploadVideo(file);
        return R.ok().message("视频上传成功").data("videoId", videoId);
    }

    @PostMapping("/reqUploadUrl")
    public R reqUploadUrl(@RequestBody VideoInfoVo videoInfoVo) {
        UploadVideoResponseVo responseVo = vodService.reqUploadUrl(videoInfoVo);
        return R.ok().data("responseVo", responseVo);
    }

    @DeleteMapping("/{id}")
    public R removeVideo(@PathVariable String id) {
        DeleteVideoResponse response = vodService.removeVideo(id);
        return R.ok().data("response", response);
    }

    @GetMapping("/getPlayAuth/{vid}")
    public R getPlayAuth(@PathVariable String vid) {
        String playAuth = vodService.getPlayAuth(vid);
        return R.ok().data("playAuth", playAuth);
    }

}
