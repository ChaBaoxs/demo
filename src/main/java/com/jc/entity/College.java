package com.jc.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_college")
public class College {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String address;
    private Double addressLng;
    private Double addressLat;

    private String phone;

    private Boolean enable;

    private String avatarUrl;

    @TableField(fill = FieldFill.INSERT)//插入时填充字段
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)//插入和更新时填充字段
    private LocalDateTime updateTime;

}
