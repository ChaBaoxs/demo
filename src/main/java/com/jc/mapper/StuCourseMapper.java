package com.jc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jc.entity.Course;
import com.jc.entity.Order;
import com.jc.entity.RoleMenu;
import com.jc.entity.StuCourse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StuCourseMapper extends BaseMapper<StuCourse> {
    List<StuCourse> findStuCourse(@Param("id") Long id);

    List<StuCourse> findById(@Param("studentId")Long studentId);
}
