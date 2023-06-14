package com.jc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jc.common.Result;
import com.jc.entity.Employee;
import com.jc.entity.Order;
import com.jc.entity.Plan;
import com.jc.entity.StuCourse;
import com.jc.service.OrderService;
import com.jc.service.StuCourseService;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @Resource
    private StuCourseService stuCourseService;
    /**
     * 获取未读消息
     * @return
     */
    @GetMapping("/isDelete")
    public Result findIs(){
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("isdetele",0);
        return Result.success(orderService.count(queryWrapper));
    }

    /**
     * 更新未读消息
     * @return
     */
    @GetMapping("/updateIs/{studentId}")
    public Result updateIs(@PathVariable Long studentId){
        QueryWrapper<StuCourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_id",studentId);
        long count = stuCourseService.count(queryWrapper);

        if (count>0){
            UpdateWrapper<Order> wrapper  = new UpdateWrapper<>();
            wrapper.eq("student_id",studentId);
            wrapper .set("isdetele",1);
            orderService.update(wrapper);
        }

        if (count == 0){
            UpdateWrapper<Order> wrapper  = new UpdateWrapper<>();
            wrapper.eq("student_id",studentId);
            wrapper .set("isdetele",0);
            orderService.update(wrapper);
        }

        return Result.success();
    }

    /**
     * 删除订单
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable int id){
        orderService.removeById(id);
        return Result.success();
    }

    /**
     * 查询学生参与计划是否结束
     * @param id
     * @return
     */
    @GetMapping("/status/{id}")
    public Result findStatus(@PathVariable Long id){
        return Result.success(  orderService.findStatus(id));
    }

    /**
     * 是否评价计划
     * @param id
     * @return
     */
    @GetMapping("/isComment/{id}")
    public Result isComment(@PathVariable Long id){
        UpdateWrapper<Order> wrapper  = new UpdateWrapper<>();
        wrapper.eq("student_id",id);
        wrapper .set("iscomment",1);
        orderService.update(wrapper);
        return Result.success();
    }

    @GetMapping("/total")
    public Result findUserTotal(){
        return Result.success(orderService.count());
    }

    /**
     * 总收益
     * @return
     */
    @GetMapping("/allPrice")
    public Result allPrice(){
        return Result.success(orderService.allPrice());
    }
}
