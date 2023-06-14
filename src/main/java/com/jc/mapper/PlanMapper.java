package com.jc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jc.entity.Plan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PlanMapper extends BaseMapper<Plan> {

    void setStudentPlan(@Param("planId") Integer planId,@Param("studentId") Long studentId);

    Integer countIsDelete();

    void updateIsDelete();
}
