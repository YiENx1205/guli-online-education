package com.atguigu.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.atguigu.oss.service.OssService;
import com.atguigu.oss.utils.AliOssConstant;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author quan
 * @date 2021-08-02
 */
@Service
public class OssServiceImpl implements OssService {

    @Override
    public String upload(MultipartFile file, String fileHost) {
        //获取阿里云存储相关常量
        String endPoint = AliOssConstant.END_POINT;
        String accessKeyId = AliOssConstant.ACCESS_KEY_ID;
        String accessKeySecret = AliOssConstant.ACCESS_KEY_SECRET;
        String bucketName = AliOssConstant.BUCKET_NAME;

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);

        //判断oss实例是否存在：如果不存在则创建，如果存在则获取
        if (!ossClient.doesBucketExist(bucketName)) {
            //创建bucket
            ossClient.createBucket(bucketName);
            //设置oss实例的访问权限：公共读
            ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        }

        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 构造文件路径：avatar/2020/1/1/文件名.jpg
        String filePath = new DateTime().toString("yyyy/MM/dd");

        // 文件名: uuid.扩展名
        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString();
        assert originalFilename != null;
        // 文件扩展名
        String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fullName = fileName + fileType;
        // 构造文件路径：avatar/2020/1/1/文件名.jpg
        String fileUrl = fileHost + "/" + filePath + "/" + fullName;

        // 依次填写Bucket名称（例如examplebucket）和Object完整路径（例如exampledir/exampleobject.txt）。Object完整路径中不能包含Bucket名称。
        ossClient.putObject(bucketName, fileUrl, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();

        //获取url地址
        String uploadUrl = "https://" + bucketName + "." + endPoint + "/" + fileUrl;
        return uploadUrl;
    }
}
