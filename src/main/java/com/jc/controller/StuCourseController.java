package com.jc.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jc.SendMail.MailRequest;
import com.jc.SendMail.SendMailService;
import com.jc.common.Result;
import com.jc.controller.dto.CourseDTO;
import com.jc.entity.Course;
import com.jc.entity.Order;
import com.jc.entity.StuCourse;
import com.jc.service.CourseService;
import com.jc.service.StuCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/stuCourse")
public class StuCourseController {

    @Resource
    private StuCourseService stuCourseService;

    @Resource
    private CourseService courseService;

    @Autowired
    private SendMailService sendMailService;


    /**
     * 为学生分配课程
     * @param stuCourse
     * @return
     */
    @PostMapping("/courses/{studentId}")
    public Result saveCourses(@PathVariable Long studentId,@RequestBody List<StuCourse> stuCourse){
        stuCourseService.saveAll(studentId,stuCourse);
        foreachStu(stuCourse);
        return Result.success();
    }

    public void foreachStu(List<StuCourse> stuCourse){
        for (StuCourse stuCourse1:stuCourse) {
            Integer courseId = stuCourse1.getCourseId();
            QueryWrapper<StuCourse> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("course_id",courseId);
            long count = stuCourseService.count(queryWrapper);
            if (count == 1){
                CourseDTO course = courseService.findById(courseId);
                MailRequest mailRequest = new MailRequest();
                mailRequest.setSendTo(course.getEmail());
                mailRequest.setSubject("课程更改通知");
                mailRequest.setText(course.getTeacherName()+"老师"+"\n您好！您已经被安排《"+course.getName()+"》课程。"+"\n课程地点："+course.getRoom()+
                        "\n课程时间："+course.getWeekday()+" 第"+course.getSection()+"大节"+
                        "\n请注意检查课表更新");
                sendMailService.sendSimpleMail(mailRequest);
            }
        }
    }

    /**
     * 学生课表显示
     * @param studentId
     * @return
     */
    @GetMapping("/courseTable/{studentId}")
    public Result findCourseTable(@PathVariable Long studentId){
        List<StuCourse> stuCourses = stuCourseService.findById(studentId);
        //格式：{section:{num:'第一大节',time:'08-09'},mon:{course:'大学物理',room:'A456',teacher:'史地从'}}
        List<JSONObject> list = CollUtil.newArrayList();
        JSONObject jsonObject1 = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        JSONObject jsonObject3 = new JSONObject();
        JSONObject jsonObject4 = new JSONObject();
        JSONObject jsonObject5 = new JSONObject();
        jsonObject1.set("section",JSONUtil.parseObj("{\"num\":\"第一大节\",\"time\":\"08:00-09:40\"}"));
        jsonObject2.set("section",JSONUtil.parseObj("{\"num\":\"第二大节\",\"time\":\"09:50-11:30\"}"));
        jsonObject3.set("section",JSONUtil.parseObj("{\"num\":\"第三大节\",\"time\":\"14:00-15:40\"}"));
        jsonObject4.set("section",JSONUtil.parseObj("{\"num\":\"第四大节\",\"time\":\"15:50-17:30\"}"));
        jsonObject5.set("section",JSONUtil.parseObj("{\"num\":\"第五大节\",\"time\":\"19:00-20:40\"}"));
        list.add(jsonObject1);
        list.add(jsonObject2);
        list.add(jsonObject3);
        list.add(jsonObject4);
        list.add(jsonObject5);
        stuCourses.forEach(mc ->{
            Integer courseId = mc.getCourseId();
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


    @GetMapping("/courses/{studentId}")
    public Result findById(@PathVariable Long studentId){
        List<StuCourse> stuCourses = stuCourseService.findById(studentId);
        return Result.success(stuCourses);
    }

    /**
     * 课程评分
     * @param stuCourse
     * @return
     */
    @PostMapping("/courseScore")
    public Result saveScore(@RequestBody StuCourse stuCourse){
        UpdateWrapper<StuCourse> wrapper  = new UpdateWrapper<>();
        wrapper.eq("student_id",stuCourse.getStudentId());
        wrapper.eq("course_id",stuCourse.getCourseId());
        wrapper .set("course_score",stuCourse.getCourseScore());
        stuCourseService.update(wrapper);
        return Result.success();
    }

    /**
     * 是否评分
     * @param studentId
     * @return
     */
    @GetMapping("/isScore/{studentId}")
    public Result isScore(@PathVariable Long studentId){
        QueryWrapper<StuCourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_id",studentId);
        queryWrapper.isNull("course_score");
        long count = stuCourseService.count(queryWrapper);
        return Result.success(count);
    }



}
