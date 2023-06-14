package com.jc.controller.dto;

import com.jc.entity.Employee;
import lombok.Data;

@Data
public class StudentDTO extends Employee {

    private String planId;

    private Integer isDetele;

    private String planName;

    private String period;

    private Long studentId;
}
