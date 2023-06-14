package com.jc.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jc.common.Result;
import com.jc.entity.Article;
import com.jc.entity.Comment;
import com.jc.service.ArticleService;
import com.jc.service.CommentService;
import com.jc.untils.TokenUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @Resource
    private CommentService commentService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Article article) {
        if (article.getId() == null) { // 新增
            article.setUser(TokenUtils.getCurrentUser().getName());
        }
        articleService.saveOrUpdate(article);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id",id);
        commentService.remove(queryWrapper);

        articleService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        articleService.removeByIds(ids);
        return Result.success();
    }

//    @GetMapping
//    public Result findAll(@RequestParam(required = false) String start, @RequestParam(required = false) String end) {
//        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
//
//        if (StrUtil.isNotBlank(start)) {
//            // where time >= start
//            queryWrapper.ge("time", start);
//        }
//        if (StrUtil.isNotBlank(end)) {
//            // where time <= end
//            queryWrapper.le("time", end);
//        }
//        return Result.success(articleService.list(queryWrapper));
//    }

    @GetMapping
    public Result findAll(){
        return Result.success(articleService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(articleService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (StrUtil.isNotBlank(name)) {
            queryWrapper.like("name", name);
        }
        return Result.success(articleService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}

