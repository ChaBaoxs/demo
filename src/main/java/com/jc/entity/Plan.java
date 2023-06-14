package com.jc.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_plan")
public class Plan {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;
    private String period;
    private String introduce;
    private String coverUrl;
    private Double score;
    private Integer price;
    private Integer status;
    private Integer trainNumber;
    private Boolean enable;
    private String beginTime;

    @TableField(fill = FieldFill.INSERT)//插入时填充字段
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)//插入和更新时填充字段
    private LocalDateTime updateTime;


    @TableField(exist = false)
    private Integer isComment;

}
