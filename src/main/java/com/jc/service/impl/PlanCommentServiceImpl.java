package com.jc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jc.entity.Order;
import com.jc.entity.PlanComment;
import com.jc.mapper.OrderMapper;
import com.jc.mapper.PlanCommentMapper;
import com.jc.service.OrderService;
import com.jc.service.PlanCommentService;
import org.springframework.stereotype.Service;

@Service
public class PlanCommentServiceImpl extends ServiceImpl<PlanCommentMapper, PlanComment> implements PlanCommentService {

    @Override
    public Long avgScore(Integer planId) {
        return null;
    }
}
