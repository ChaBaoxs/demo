package com.jc.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_order")
public class Order {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer planId;

    private Long studentId;

    private Integer isdetele;

    private Integer iscomment;

    @TableField(fill = FieldFill.INSERT)//插入时填充字段
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)//插入和更新时填充字段
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private Integer sum;
}
