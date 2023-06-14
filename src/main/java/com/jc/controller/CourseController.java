package com.jc.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jc.common.Result;
import com.jc.controller.dto.CourseDTO;
import com.jc.entity.*;
import com.jc.service.CourseService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Resource
    private CourseService courseService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Course course) {
        return Result.success(courseService.saveOrUpdate(course));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success( courseService.removeById(id));
    }

    @DeleteMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(courseService.removeByIds(ids));
    }

    @GetMapping
    public Result findAll() {
        return Result.success(courseService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(courseService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
//        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
//        queryWrapper.like("name", name);
//        queryWrapper.orderByDesc("id");
//        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
//        queryWrapper.orderByDesc("id");
//        Page<Course> page = courseService.page(new Page<>(pageNum, pageSize), queryWrapper);
//        List<Course> records = page.getRecords();
//        for (Course record : records) {
//            User user = userService.getById(record.getTeacherId());
//            if(user != null) {
//                record.setTeacher(user.getNickname());
//            }
//
//        }
        Page<Course> page = courseService.findPage(new Page<>(pageNum, pageSize), name);
        return Result.success(page);
    }

    /**
     * 根据id修改图片信息
     * @param course
     * @return
     */
    @PostMapping("/update")
    public Result update(@RequestBody Course course){
        return Result.success(courseService.updateById(course));
    }

    /**
     * 查询课程总数
     * @return
     */
    @GetMapping("/total")
    public Result findUserTotal(){
        return Result.success(courseService.count());
    }

    /**
     * 查询所有在线课程信息
     * @return
     */
    @GetMapping("/courses")
    public Result findCourses(){
        return Result.success(courseService.findCourses());
    }

    /**
     * 老师课表显示
     * @param teacherId
     * @return
     */
    @GetMapping("/courseTable/{teacherId}")
    public Result findCourseTable(@PathVariable Long teacherId){
        List<CourseDTO> teaCourses = courseService.findAllById(teacherId);
        //格式：{section:{num:'第一大节',time:'08-09'},mon:{course:'大学物理',room:'A456',teacher:'史地从'}}
        List<JSONObject> list = CollUtil.newArrayList();
        JSONObject jsonObject1 = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        JSONObject jsonObject3 = new JSONObject();
        JSONObject jsonObject4 = new JSONObject();
        JSONObject jsonObject5 = new JSONObject();
        jsonObject1.set("section", JSONUtil.parseObj("{\"num\":\"第一大节\",\"time\":\"08:00-09:40\"}"));
        jsonObject2.set("section",JSONUtil.parseObj("{\"num\":\"第二大节\",\"time\":\"09:50-11:30\"}"));
        jsonObject3.set("section",JSONUtil.parseObj("{\"num\":\"第三大节\",\"time\":\"14:00-15:40\"}"));
        jsonObject4.set("section",JSONUtil.parseObj("{\"num\":\"第四大节\",\"time\":\"15:50-17:30\"}"));
        jsonObject5.set("section",JSONUtil.parseObj("{\"num\":\"第五大节\",\"time\":\"19:00-20:40\"}"));
        list.add(jsonObject1);
        list.add(jsonObject2);
        list.add(jsonObject3);
        list.add(jsonObject4);
        list.add(jsonObject5);
        teaCourses.forEach(mc ->{
            Integer courseId = mc.getId();
            CourseDTO course = courseService.findById(courseId);
            String weekday = mc.getWeekday();
            String section = mc.getSection();
            switch (section){
                case "一":
                    setJson(weekday,course,jsonObject1);
                    break;
                case "二":
                    setJson(weekday,course,jsonObject2);
                    break;
                case "三":
                    setJson(weekday,course,jsonObject3);
                    break;
                case "四":
                    setJson(weekday,course,jsonObject4);
                    break;
                case "五":
                    setJson(weekday,course,jsonObject5);
                    break;

            }
        });

        return Result.success(list);
    }

    private JSONObject setJson(String weekday,Course course,JSONObject jsonObject){
        if ("周一".equals(weekday)) {
            jsonObject.set("mon", course);
        } else if ("周二".equals(weekday)){
            jsonObject.set("tue", course);
        } else if ("周三".equals(weekday)){
            jsonObject.set("wes", course);
        } else if ("周四".equals(weekday)){
            jsonObject.set("thu", course);
        } else if ("周五".equals(weekday)){
            jsonObject.set("fri", course);
        } else if ("周六".equals(weekday)){
            jsonObject.set("sat", course);
        } else if ("周日".equals(weekday)){
            jsonObject.set("sun", course);
        }
        return jsonObject;
    }


}
