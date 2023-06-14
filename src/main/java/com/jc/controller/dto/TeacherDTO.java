package com.jc.controller.dto;

import com.jc.entity.Course;
import com.jc.entity.Employee;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TeacherDTO extends Employee {

    private List<Course> courses = new ArrayList<>();
}
