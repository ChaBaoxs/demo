package com.jc.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("sys_comment")
public class Comment implements Serializable {

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      private String content;

      private Long userId;

      @TableField(fill = FieldFill.INSERT)//插入时填充字段
      private LocalDateTime createTime;

      @TableField(fill = FieldFill.INSERT_UPDATE)//插入和更新时填充字段
      private LocalDateTime updateTime;

      private Integer pid;
      @TableField(exist = false)
      private String pname;  // 父节点的用户昵称
      @TableField(exist = false)
      private Long pUserId;  // 父节点的用户id

      private Integer originId;

      private Integer articleId;

      @TableField(exist = false)
      private String name;
      @TableField(exist = false)
      private String avatarUrl;

      @TableField(exist = false)
      private List<Comment> children;


}
