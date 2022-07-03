package com.atguigu.vodservice.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.HttpClientConfig;
import com.aliyuncs.profile.DefaultProfile;

/**
 * @author quan
 * @date 2021-08-03
 */
public class AliVodSdkUtil {
    public static DefaultAcsClient initVodClient(String accessKeyId, String accessKeySecret) {
        String regionId = "cn-shanghai";
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        // // 设置代理
        // HttpClientConfig clientConfig = HttpClientConfig.getDefault();
        // clientConfig.setHttpProxy("http://127.0.0.1:7890");
        // clientConfig.setHttpsProxy("http://127.0.0.1:7890");
        // profile.setHttpClientConfig(clientConfig);
        return new DefaultAcsClient(profile);
    }
}
