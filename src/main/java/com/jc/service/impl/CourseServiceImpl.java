package com.jc.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jc.controller.dto.CourseDTO;
import com.jc.entity.Course;
import com.jc.entity.StuCourse;
import com.jc.mapper.CourseMapper;
import com.jc.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public Page<Course> findPage(Page<Course> page, String name) {
        return courseMapper.findPage(page,name);
    }

    @Override
    public List<CourseDTO> findCourses() {
        return courseMapper.findCourses();
    }

    @Override
    public CourseDTO findById(Integer courseId) {
        return courseMapper.findById(courseId);
    }

    @Override
    public List<CourseDTO> findAllById(Long teacherId) {
        return courseMapper.findAllById(teacherId);
    }


}
