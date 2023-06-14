package com.jc.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_video")
public class Video {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer courseId;
    private String name;
    private String type;
    private Long size;
    private String url;
    private String coverUrl;
    private String md5;
    private Boolean enable;

    @TableField(fill = FieldFill.INSERT)//插入时填充字段
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)//插入和更新时填充字段
    private LocalDateTime updateTime;
}
