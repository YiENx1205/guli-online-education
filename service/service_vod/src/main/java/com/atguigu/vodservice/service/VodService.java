package com.atguigu.vodservice.service;

import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.atguigu.vodservice.vo.UploadVideoResponseVo;
import com.atguigu.vodservice.vo.VideoInfoVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author quan
 * @date 2021-08-04
 */
public interface VodService {
    String uploadVideo(MultipartFile file) throws IOException;

    UploadVideoResponseVo reqUploadUrl(VideoInfoVo videoInfoVo);

    DeleteVideoResponse removeVideo(String id);

    String getPlayAuth(String vid);
}
