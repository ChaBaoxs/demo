package com.jc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("sys_course")
public class Course implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;
    private String room;
    private String weekday;
    private String section;
    private Boolean state;
    private Long teacherId;

    @TableField(exist = false)
    private String teacher;

    @TableField(exist = false)
    private String email;
}
