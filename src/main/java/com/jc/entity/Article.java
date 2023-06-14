package com.jc.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("sys_article")
public class Article implements Serializable {

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      private String name;

      private String content;

      private String user;

      @TableField(fill = FieldFill.INSERT)//插入时填充字段
      private LocalDateTime createTime;

      @TableField(fill = FieldFill.INSERT_UPDATE)//插入和更新时填充字段
      private LocalDateTime updateTime;


}
