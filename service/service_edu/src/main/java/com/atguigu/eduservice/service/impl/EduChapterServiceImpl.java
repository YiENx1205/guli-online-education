package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.vo.ChapterVo;
import com.atguigu.eduservice.entity.vo.VideoVo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.mapper.EduVideoMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程章节表 服务实现类
 * </p>
 *
 * @author Quan
 * @since 2021-08-03
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {
    @Resource
    private EduChapterMapper eduChapterMapper;

    @Resource
    private EduVideoMapper eduVideoMapper;

    @Override
    public List<ChapterVo> nestedList(String courseId) {
        QueryWrapper<EduChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");
        queryWrapper.eq("course_id", courseId);
        List<EduChapter> eduChapters = eduChapterMapper.selectList(queryWrapper);
        List<ChapterVo> list = new ArrayList<>();
        for (EduChapter eduChapter : eduChapters) {
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter, chapterVo);
            // 添加章节下的视频
            QueryWrapper<EduVideo> sonWrapper = new QueryWrapper<>();
            sonWrapper.orderByAsc("sort");
            sonWrapper.eq("chapter_id", eduChapter.getId());
            List<EduVideo> eduVideos = eduVideoMapper.selectList(sonWrapper);
            ArrayList<VideoVo> videoVos = new ArrayList<>();
            for (EduVideo eduVideo : eduVideos) {
                VideoVo videoVo = new VideoVo();
                BeanUtils.copyProperties(eduVideo, videoVo);
                videoVos.add(videoVo);
            }
            chapterVo.setChildren(videoVos);
            list.add(chapterVo);
        }
        return list;
    }
}
