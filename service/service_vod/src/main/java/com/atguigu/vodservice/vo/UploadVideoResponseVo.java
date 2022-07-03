package com.atguigu.vodservice.vo;

import lombok.Data;

/**
 * @author quan
 * @date 2021-08-04
 */
@Data
public class UploadVideoResponseVo {
    private String requestId;
    private String videoId;
    private String uploadAddress;
    private String uploadAuth;

}
