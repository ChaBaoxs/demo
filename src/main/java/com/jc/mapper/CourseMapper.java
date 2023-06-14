package com.jc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jc.controller.dto.CourseDTO;
import com.jc.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {

    Page<Course> findPage(Page<Course> page, @Param("name") String name);

    List<CourseDTO> findCourses();

    void saveCourses(Integer c, Long studentId);

    CourseDTO findById(Integer courseId);

    List<CourseDTO> findAllById(Long teacherId);
}