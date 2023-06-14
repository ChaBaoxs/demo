package com.jc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jc.controller.dto.CourseDTO;
import com.jc.controller.dto.VideoDTO;
import com.jc.entity.Course;
import com.jc.entity.StuCourse;

import java.util.List;

public interface CourseService extends IService<Course> {
    Page<Course> findPage(Page<Course> page, String name);

    List<CourseDTO> findCourses();

    CourseDTO findById(Integer courseId);

    List<CourseDTO> findAllById(Long teacherId);
}
