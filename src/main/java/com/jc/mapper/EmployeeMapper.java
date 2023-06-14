package com.jc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jc.controller.dto.PasswordDTO;
import com.jc.controller.dto.StudentDTO;
import com.jc.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

    Page<StudentDTO> findPage(Page<StudentDTO> page, String username,Integer isDelete,String planName);

    @Update("update employee set password = #{newPassword} where username = #{username} and password = #{password}")
    int updatePassword(PasswordDTO passwordDTO);
}
