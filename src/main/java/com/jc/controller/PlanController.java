package com.jc.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jc.common.Constants;
import com.jc.common.Result;
import com.jc.entity.Employee;
import com.jc.entity.Order;
import com.jc.entity.Plan;
import com.jc.entity.Video;
import com.jc.exception.ServiceException;
import com.jc.service.OrderService;
import com.jc.service.PlanService;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/plan")
public class PlanController {

    @Resource
    private PlanService planService;

    @Resource
    private OrderService orderService;


    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Plan plan) {
        return Result.success(planService.saveOrUpdate(plan));
    }

    /**
     * 加入课程
     * @param planId
     * @param studentId
     * @return
     */
    @PostMapping("/studentPlan/{planId}/{studentId}")
    public Result studentPlan(@PathVariable Integer planId,@PathVariable Long studentId) {
        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("plan_id",planId);
        queryWrapper1.eq("student_id",studentId);
        if (orderService.count(queryWrapper1)>0){
            throw new ServiceException(Constants.CODE_600, "你已参加该计划，请勿重复加入");
        }
        planService.setStudentPlan(planId,studentId);
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("plan_id",planId);
        int count = (int) orderService.count(queryWrapper);
        UpdateWrapper<Plan> wrapper  = new UpdateWrapper<>();
        wrapper .eq("id", planId);
        wrapper.set("train_number", count);
        planService.update(wrapper);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success( planService.removeById(id));
    }

    @DeleteMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(planService.removeByIds(ids));
    }

    @GetMapping
    public Result findAll() {
        QueryWrapper<Plan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("enable",1);
        return Result.success(planService.list(queryWrapper));
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(planService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        //构造分页构造器
        Page page = new Page(pageNum,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Plan> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        //StringUtils.isNotEmpty(name)类似if(name!= null)
        queryWrapper.like(StringUtils.isNotEmpty(name),Plan::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Plan::getCreateTime);
        //执行查询


        return Result.success(planService.page(page,queryWrapper));
    }

    @PostMapping("/update")
    public Result update(@RequestBody Plan plan){
        return Result.success(planService.saveOrUpdate(plan));
    }

    /**
     * 根据id修改计划状态
     * @param plan
     * @return
     */
    @PutMapping
    public Result updateStatus(@RequestBody Plan plan){
        if(plan.getStatus()==0){
            UpdateWrapper<Plan> wrapper  = new UpdateWrapper<>();
            wrapper.eq("id",plan.getId());
            wrapper .set("enable",0);
            planService.update(wrapper);
        }
        return Result.success(planService.updateById(plan));
    }


//    /**
//     * 查询未读学生信息
//     * @return
//     */
//    @PostMapping("/msg")
//    public Result Msg(){
//        return Result.success( planService.findstuMsg());
//    }
}
