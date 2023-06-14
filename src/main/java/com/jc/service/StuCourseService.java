package com.jc.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jc.entity.Order;
import com.jc.entity.Role;
import com.jc.entity.StuCourse;

import java.util.List;

public interface StuCourseService extends IService<StuCourse> {

    List<StuCourse> findStuCourse(Long id);

    void saveAll( Long studentId,List<StuCourse> stuCourse);

    List<StuCourse> findById(Long studentId);
}
