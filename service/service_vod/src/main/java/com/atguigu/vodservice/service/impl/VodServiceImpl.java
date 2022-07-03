package com.atguigu.vodservice.service.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.*;
import com.atguigu.servicebase.handler.exception.CustomException;
import com.atguigu.vodservice.service.VodService;
import com.atguigu.vodservice.utils.AliVodSdkUtil;
import com.atguigu.vodservice.vo.UploadVideoResponseVo;
import com.atguigu.vodservice.vo.VideoInfoVo;
import com.atguigu.vodservice.utils.AliAccountConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author quan
 * @date 2021-08-04
 */
@Service
@Slf4j
public class VodServiceImpl implements VodService {

    @Override
    public String uploadVideo(MultipartFile file) {
        return null;
    }

    @Override
    public UploadVideoResponseVo reqUploadUrl(VideoInfoVo videoInfoVo) {
        try {
            DefaultAcsClient client = AliVodSdkUtil.initVodClient(AliAccountConstant.ACCESS_KEY_ID, AliAccountConstant.ACCESS_KEY_SECRET);

            String title = videoInfoVo.getTitle();
            String fileName = videoInfoVo.getFileName();
            Long fileSize = videoInfoVo.getFileSize();
            CreateUploadVideoRequest request = new CreateUploadVideoRequest();
            request.setTitle(title);
            request.setFileName(fileName);
            request.setFileSize(fileSize);

            CreateUploadVideoResponse response = client.getAcsResponse(request);
            System.out.print("VideoId = " + response.getVideoId() + "\n");
            System.out.print("UploadAddress = " + response.getUploadAddress() + "\n");
            System.out.print("UploadAuth = " + response.getUploadAuth() + "\n");
            UploadVideoResponseVo responseVo = new UploadVideoResponseVo();
            BeanUtils.copyProperties(response, responseVo);
            return responseVo;
        } catch (Exception e) {
            throw new CustomException(20002, "创建失败");
        }
    }

    @Override
    public DeleteVideoResponse removeVideo(String id) {
        try {
            DefaultAcsClient client = AliVodSdkUtil.initVodClient(AliAccountConstant.ACCESS_KEY_ID, AliAccountConstant.ACCESS_KEY_SECRET);
            DeleteVideoRequest request = new DeleteVideoRequest();
            request.setVideoIds(id);
            return client.getAcsResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
            throw new CustomException(20002, "删除失败");
        }
    }

    @Override
    public String getPlayAuth(String vid) {
        try {
            DefaultAcsClient client = AliVodSdkUtil.initVodClient(AliAccountConstant.ACCESS_KEY_ID, AliAccountConstant.ACCESS_KEY_SECRET);
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            GetVideoPlayAuthResponse response;

            request.setVideoId(vid);
            response = client.getAcsResponse(request);
            String playAuth = response.getPlayAuth();
            System.out.println("playUrl: " + response.getPlayAuth());
            return playAuth;
        } catch (ClientException e) {
            e.printStackTrace();
            throw new CustomException(20001, "获取失败");
        }
    }
}
