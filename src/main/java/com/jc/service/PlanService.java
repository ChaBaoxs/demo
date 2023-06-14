package com.jc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jc.entity.Plan;

public interface PlanService extends IService<Plan> {
    void setStudentPlan(Integer planId, Long studentId);

    Integer countIsDelete();

    void updateIsDelete();
}
