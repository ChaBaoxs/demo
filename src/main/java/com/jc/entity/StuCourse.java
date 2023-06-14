package com.jc.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("sys_stu_course")
@Data
public class StuCourse {

    private Long studentId;

    private Integer courseId;

    private Integer courseScore;

    @TableField(exist = false)
    private String name;

    @TableField(exist = false)
    private String weekday;

    @TableField(exist = false)
    private String section;

    @TableField(exist = false)
    private String room;

    @TableField(exist = false)
    private String teacher;

    @TableField(exist = false)
    private Long score;
}
