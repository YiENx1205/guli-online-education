package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.vo.ExcelSubjectData;
import com.atguigu.eduservice.entity.vo.SubjectNestedVo;
import com.atguigu.eduservice.entity.vo.SubjectVo;
import com.atguigu.eduservice.listener.SubjectExcelListener;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author Quan
 * @since 2021-08-02
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {
    @Resource
    private EduSubjectMapper eduSubjectMapper;


    @Override
    public void saveSubject(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            EasyExcel.read(inputStream, ExcelSubjectData.class, new SubjectExcelListener(this)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<SubjectNestedVo> getNestedList() {
        List<SubjectNestedVo> list = new ArrayList<>();
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("sort");
        wrapper.eq("parent_id", "0");
        List<EduSubject> parentEduSubjects = eduSubjectMapper.selectList(wrapper);
        for (EduSubject parentEduSubject : parentEduSubjects) {
            QueryWrapper<EduSubject> sonWrap = new QueryWrapper<>();
            sonWrap.orderByAsc("sort");
            sonWrap.eq("parent_id", parentEduSubject.getId());
            List<EduSubject> sonEduSubjects = eduSubjectMapper.selectList(sonWrap);
            ArrayList<SubjectVo> subjectVos = new ArrayList<>();
            for (EduSubject sonEduSubject : sonEduSubjects) {
                SubjectVo subjectVo = new SubjectVo();
                // 将sonEduSubject的id、title复制到封装vo去
                BeanUtils.copyProperties(sonEduSubject, subjectVo);
                subjectVos.add(subjectVo);
            }
            SubjectNestedVo subjectNestedVo = new SubjectNestedVo();
            // 将parentEduSubject的id、title复制到封装vo去
            BeanUtils.copyProperties(parentEduSubject, subjectNestedVo);
            // 还有个Children要自己添加
            subjectNestedVo.setChildren(subjectVos);
            list.add(subjectNestedVo);
        }
        return list;
    }
}
