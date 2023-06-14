package com.jc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jc.common.Result;
import com.jc.controller.dto.EmployeeDTO;
import com.jc.entity.Employee;
import com.jc.entity.Plan;
import com.jc.entity.PlanComment;
import com.jc.entity.StuCourse;
import com.jc.service.CourseService;
import com.jc.service.PlanCommentService;
import com.jc.service.PlanService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/planComment")
public class PlanCommentController {

    @Resource
    private PlanCommentService planCommentService;

    @Resource
    private PlanService planService;
    /**
     * 保存计划评价
     * @return
     */
    @PostMapping("/planScore")
    public Result save(@RequestBody PlanComment planComment){
        planCommentService.save(planComment);
        Integer planId = planComment.getPlanId();
//        planCommentService.avgScore(planId);
        QueryWrapper<PlanComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("avg(score) as all_score");
        queryWrapper.eq("plan_id",planId);
        PlanComment one = planCommentService.getOne(queryWrapper);
        UpdateWrapper<Plan> wrapper  = new UpdateWrapper<>();
        wrapper.eq("id",planId);
        wrapper.set("score",one.getAllScore());
        planService.update(wrapper);
        return Result.success();
    }
}
