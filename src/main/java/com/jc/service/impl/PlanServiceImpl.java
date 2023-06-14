package com.jc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jc.entity.Plan;
import com.jc.mapper.PlanMapper;
import com.jc.service.PlanService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PlanServiceImpl extends ServiceImpl<PlanMapper, Plan> implements PlanService {

    @Resource
    private PlanMapper planMapper;

    @Override
    public void setStudentPlan(Integer planId, Long studentId) {
        planMapper.setStudentPlan(planId,studentId);
    }

    @Override
    public Integer countIsDelete() {
        return planMapper.countIsDelete();
    }

    @Override
    public void updateIsDelete() {
        planMapper.updateIsDelete();
    }
}
