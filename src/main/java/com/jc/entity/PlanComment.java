package com.jc.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("sys_plan_comment")
@Data
public class PlanComment {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Long studentId;

    private Integer planId;

    private Long score;

    private String comment;

    @TableField(fill = FieldFill.INSERT)//插入时填充字段
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)//插入和更新时填充字段
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private String name;

    @TableField(exist = false)
    private Double allScore;
}
