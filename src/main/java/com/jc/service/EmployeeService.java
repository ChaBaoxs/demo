package com.jc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jc.common.Result;
import com.jc.controller.dto.EmployeeDTO;
import com.jc.controller.dto.PasswordDTO;
import com.jc.controller.dto.StudentDTO;
import com.jc.controller.dto.TeacherDTO;
import com.jc.entity.Employee;

import javax.servlet.http.HttpServletRequest;

public interface EmployeeService extends IService<Employee> {

    EmployeeDTO login(EmployeeDTO employeeDTO);

    Employee saveAll(Employee employee);

    Employee updateWithId(Employee employee);

    Page<StudentDTO> findPage(Page<StudentDTO> page, String username,Integer isDelete,String planName);

    Employee register(EmployeeDTO employeeDTO);

    void updatePassword(PasswordDTO passwordDTO);
}
