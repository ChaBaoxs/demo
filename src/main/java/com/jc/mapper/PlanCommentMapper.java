package com.jc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jc.entity.Order;
import com.jc.entity.PlanComment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PlanCommentMapper extends BaseMapper<PlanComment> {
}
