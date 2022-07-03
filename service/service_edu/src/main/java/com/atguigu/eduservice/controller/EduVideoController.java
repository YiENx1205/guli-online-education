package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.commonutils.ResultCode;
import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.handler.exception.CustomException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 课程小结表 前端控制器
 * </p>
 *
 * @author Quan
 * @since 2021-08-03
 */
@RestController
@RequestMapping("/eduservice/video")
public class EduVideoController {
    @Resource
    private EduVideoService eduVideoService;

    @Resource
    private VodClient vodClient;

    @ApiOperation(value = "新增小节")
    @PostMapping("/addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo){
        eduVideoService.save(eduVideo);
        return R.ok();
    }

    @ApiOperation(value = "修改小节")
    @PostMapping("/editVideo")
    public R editVideo(@RequestBody EduVideo eduVideo){
        eduVideoService.updateById(eduVideo);
        return R.ok();
    }

    @ApiOperation(value = "根据ID查询视频")
    @GetMapping("/getVideoInfo/{id}")
    public R getById(
            @ApiParam(name = "id", value = "章节ID", required = true)
            @PathVariable String id){
        EduVideo video = eduVideoService.getById(id);
        return R.ok().data("video", video);
    }

    @DeleteMapping("/{id}")
    public R delById(@PathVariable String id){
        EduVideo eduVideo = eduVideoService.getById(id);
        eduVideoService.removeById(id);

        // 删除小节的同时也删除阿里云中的远程视频
        if (!StringUtils.isEmpty(eduVideo.getVideoSourceId())) {
            R result = vodClient.removeVideo(eduVideo.getVideoSourceId());
            if (result.getCode().equals(ResultCode.ERROR)) {
                throw new CustomException(20002, "触发了熔断器，可能vod服务器宕机了");
            }
        }
        return R.ok();
    }
}

