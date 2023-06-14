package com.jc.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jc.entity.Order;
import com.jc.entity.StuCourse;
import com.jc.mapper.CourseMapper;
import com.jc.mapper.StuCourseMapper;
import com.jc.service.StuCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StuCourseServiceImpl extends ServiceImpl<StuCourseMapper, StuCourse> implements StuCourseService {

    @Autowired
    private StuCourseMapper stuCourseMapper;

    @Override
    public List<StuCourse> findStuCourse(Long id) {
        return stuCourseMapper.findStuCourse(id);
    }

    @Transactional
    @Override
    public void saveAll( Long studentId,List<StuCourse> stuCourse) {
        remove(new UpdateWrapper<StuCourse>().eq("student_id",studentId));
        saveBatch(stuCourse);
    }

    @Override
    public List<StuCourse> findById(Long studentId) {
        return stuCourseMapper.findById(studentId);
    }
}
