package com.jc.controller.dto;

import com.jc.entity.Comment;
import lombok.Data;

@Data
public class CommentDTO extends Comment {

    private String username;

    private String articleName;

    private String contentName;
}
