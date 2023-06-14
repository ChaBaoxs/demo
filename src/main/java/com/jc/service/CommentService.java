package com.jc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jc.entity.Comment;

import java.util.List;

public interface CommentService extends IService<Comment> {

    List<Comment> findCommentDetail(Integer articleId);
}
