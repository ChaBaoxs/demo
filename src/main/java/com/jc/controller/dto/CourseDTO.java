package com.jc.controller.dto;

import com.jc.entity.Course;
import com.jc.entity.Menu;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CourseDTO extends Course {

    private String teacherName;

    private Long studentId;

    private Long courseId;
}
