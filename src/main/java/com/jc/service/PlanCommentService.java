package com.jc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jc.entity.Order;
import com.jc.entity.PlanComment;

public interface PlanCommentService  extends IService<PlanComment> {
    Long avgScore(Integer planId);
}
