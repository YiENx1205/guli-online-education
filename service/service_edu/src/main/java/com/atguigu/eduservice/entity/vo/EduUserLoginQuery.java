package com.atguigu.eduservice.entity.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author quan
 * @date 2021-08-01
 */
@ApiModel(value = "后台管理用户对象", description = "后台管理用户对象")
@Data
public class EduUserLoginQuery {
    String username;
    String password;
}
