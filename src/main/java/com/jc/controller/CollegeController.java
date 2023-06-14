package com.jc.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jc.common.Result;
import com.jc.entity.College;
import com.jc.entity.Course;
import com.jc.entity.Employee;
import com.jc.entity.Files;
import com.jc.service.CollegeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/college")
public class CollegeController {

    @Resource
    private CollegeService collegeService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody College college) {
        return Result.success(collegeService.saveOrUpdate(college));
    }

//    // 新增或者更新
//    @PostMapping
//    public Result updateBYId(@RequestBody College college) {
//        return Result.success(collegeService.saveOrUpdate(college));
//    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success( collegeService.removeById(id));
    }

    @DeleteMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(collegeService.removeByIds(ids));
    }

    @GetMapping
    public Result findAll() {
        QueryWrapper<College> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("enable",1);
        return Result.success(collegeService.list(queryWrapper));
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(collegeService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        //构造分页构造器
        Page page = new Page(pageNum,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<College> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        //StringUtils.isNotEmpty(name)类似if(name!= null)
        queryWrapper.like(StringUtils.isNotEmpty(name),College::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(College::getCreateTime);
        //执行查询


        return Result.success(collegeService.page(page,queryWrapper));
    }

    @PostMapping("/update")
    public Result update(@RequestBody College college){
        return Result.success(collegeService.updateById(college));
    }
}
