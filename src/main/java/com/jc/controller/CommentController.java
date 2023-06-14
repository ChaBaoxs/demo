package com.jc.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jc.common.Result;
import com.jc.controller.dto.CommentDTO;
import com.jc.controller.dto.TeacherDTO;
import com.jc.entity.Article;
import com.jc.entity.Comment;
import com.jc.entity.Course;
import com.jc.entity.Employee;
import com.jc.service.ArticleService;
import com.jc.service.CommentService;
import com.jc.service.EmployeeService;
import com.jc.untils.TokenUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    @Resource
    private ArticleService articleService;

    @Resource
    private EmployeeService employeeService;



    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Comment comment) {
        if (comment.getId() == null) { // 新增评论
            comment.setUserId(TokenUtils.getCurrentUser().getId());
            if (comment.getPid() != null) {  // 判断如果是回复，进行处理
                Integer pid = comment.getPid();
                Comment pComment = commentService.getById(pid);
                if (pComment.getOriginId() != null) {  // 如果当前回复的父级有祖宗，那么就设置相同的祖宗
                    comment.setOriginId(pComment.getOriginId());
                } else {  // 否则就设置父级为当前回复的祖宗
                    comment.setOriginId(comment.getPid());
                }
            }

        }
        commentService.saveOrUpdate(comment);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        commentService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        commentService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(commentService.list());
    }

    @GetMapping("/tree/{articleId}")
    public Result findTree(@PathVariable Integer articleId) {
        List<Comment> articleComments = commentService.findCommentDetail(articleId);  // 查询所有的评论和回复数据
        // 查询评论数据（不包括回复）
        List<Comment> originList = articleComments.stream().filter(comment -> comment.getOriginId() == null).collect(Collectors.toList());

        // 设置评论数据的子节点，也就是回复内容
        for (Comment origin : originList) {
            List<Comment> comments = articleComments.stream().filter(comment -> origin.getId().equals(comment.getOriginId())).collect(Collectors.toList());  // 表示回复对象集合
            comments.forEach(comment -> {
                Optional<Comment> pComment = articleComments.stream().filter(c1 -> c1.getId().equals(comment.getPid())).findFirst();  // 找到当前评论的父级
                pComment.ifPresent((v -> {  // 找到父级评论的用户id和用户昵称，并设置给当前的回复对象
                    comment.setPUserId(v.getUserId());
                    comment.setPname(v.getName());
                }));
            });
            origin.setChildren(comments);
        }
        return Result.success(originList);
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(commentService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,@RequestParam String articleId1) {

        //构造分页构造器
        Page page = new Page(pageNum,pageSize);
        Page<CommentDTO> commentDTOPage = new Page<>();
        //构造条件构造器
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        //StringUtils.isNotEmpty(name)类似if(name!= null)
        queryWrapper.eq(StringUtils.isNotEmpty(articleId1),Comment::getArticleId,articleId1);
        //添加排序条件
        queryWrapper.orderByDesc(Comment::getCreateTime);
        //执行查询
        commentService.page(page,queryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(page,commentDTOPage,"records");

        List<Comment> records = page.getRecords();
        //将records处理,item代表comment,遍历出来的每一个教师对象
        //需要返回一个CommentDTO类型对象
        List<CommentDTO> list = records.stream().map((item)->{
            CommentDTO commentDTO = new CommentDTO();
            //new出来的DTO其他值为空，需要拷贝
            BeanUtils.copyProperties(item,commentDTO);
            Integer articleId = item.getArticleId();//文章id
            LambdaQueryWrapper<Article> articleQueryWrapper = new LambdaQueryWrapper();
            articleQueryWrapper.eq(Article::getId,articleId);
            Article article = articleService.getOne(articleQueryWrapper);
            commentDTO.setArticleName(article.getName());
            commentDTO.setUsername(article.getUser());

//            //查询文章
//            String articleName1 = commentDTO.getArticleName();
//            if (articleName!=null){
//                if (!articleName1.contains(articleName)){
//                   return null;
//                }
//            }

            Long userId = item.getUserId();//评论人id
            LambdaQueryWrapper<Employee> employeeQueryWrapper = new LambdaQueryWrapper();
            employeeQueryWrapper.eq(Employee::getId,userId);
            Employee employee = employeeService.getOne(employeeQueryWrapper);
            commentDTO.setContentName(employee.getName());

            return commentDTO;
        }).collect(Collectors.toList());//遍历后需要收集转成集合
//        list.removeAll(Arrays.asList("", null));
        commentDTOPage.setRecords(list);
        return Result.success(commentDTOPage);
    }

}

