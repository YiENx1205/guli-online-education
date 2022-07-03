package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduComment;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 评论 服务类
 * </p>
 *
 * @author Quan
 * @since 2021-08-07
 */
public interface EduCommentService extends IService<EduComment> {

    Map<String, Object> listByCourseId(Page<EduComment> eduCommentPage, String courseId);
}
