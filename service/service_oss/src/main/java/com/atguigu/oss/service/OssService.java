package com.atguigu.oss.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author quan
 * @date 2021-08-02
 */
public interface OssService {
    String upload(MultipartFile file, String fileHost);
}
