package com.jc.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.jc.common.Constants;
import com.jc.common.Result;
import com.jc.entity.Employee;
import com.jc.entity.Files;
import com.jc.entity.Order;
import com.jc.mapper.FileMapper;
import com.jc.service.EmployeeService;
import com.jc.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private OrderService orderService;

    @Resource
    private FileMapper fileMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/example")
    public Result get(){
        Map<String,Object> map = new HashMap<>();
        map.put("x", CollUtil.newArrayList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));
        map.put("y", CollUtil.newArrayList(150, 230, 224, 218, 135, 147, 260));
        return Result.success(map);
    }

    /**
     * 各季度会员数
     * @return
     */
     @GetMapping("/members")
     public Result members(){
         List<Employee> list = employeeService.list();
         int q1 = 0; // 第一季度
         int q2 = 0; // 第二季度
         int q3 = 0; // 第三季度
         int q4 = 0; // 第四季度
         for (Employee employee : list) {

             // 从默认时区的系统时钟获取当前的日期时间
             LocalDateTime createTime = employee.getCreateTime();
             // 将此日期时间与时区相结合以创建 ZonedDateTime
             ZonedDateTime zonedDateTime = createTime.atZone(ZoneId.systemDefault());
             // 本地时间线LocalDateTime到即时时间线Instant时间戳
             Instant instant = zonedDateTime.toInstant();
             // UTC时间(世界协调时间,UTC + 00:00)转北京(北京,UTC + 8:00)时间
             Date date = Date.from(instant);

             Quarter quarter = DateUtil.quarterEnum(date);
             switch (quarter) {
                 case Q1: q1 += 1; break;
                 case Q2: q2 += 1; break;
                 case Q3: q3 += 1; break;
                 case Q4: q4 += 1; break;
                 default: break;
             }
         }
         return Result.success(CollUtil.newArrayList(q1, q2, q3, q4));
     }

    /**
     * 各季度成交单数
     * @return
     */
    @GetMapping("/orders")
    public Result orders(){
        List<Order> list = orderService.list();
        int q1 = 0; // 第一季度
        int q2 = 0; // 第二季度
        int q3 = 0; // 第三季度
        int q4 = 0; // 第四季度
        for (Order order : list) {

            // 从默认时区的系统时钟获取当前的日期时间
            LocalDateTime createTime = order.getCreateTime();
            // 将此日期时间与时区相结合以创建 ZonedDateTime
            ZonedDateTime zonedDateTime = createTime.atZone(ZoneId.systemDefault());
            // 本地时间线LocalDateTime到即时时间线Instant时间戳
            Instant instant = zonedDateTime.toInstant();
            // UTC时间(世界协调时间,UTC + 00:00)转北京(北京,UTC + 8:00)时间
            Date date = Date.from(instant);

            Quarter quarter = DateUtil.quarterEnum(date);
            switch (quarter) {
                case Q1: q1 += 1; break;
                case Q2: q2 += 1; break;
                case Q3: q3 += 1; break;
                case Q4: q4 += 1; break;
                default: break;
            }
        }
        return Result.success(CollUtil.newArrayList(q1, q2, q3, q4));
    }

    @GetMapping("/file/front/all")
    public Result frontAll() {
        // 1. 从缓存获取数据
        String jsonStr = stringRedisTemplate.opsForValue().get(Constants.FILES_KEY);
        List<Files> files;
        if (StrUtil.isBlank(jsonStr)) {  // 2. 取出来的json是空的
            files = fileMapper.selectList(null);  // 3. 从数据库取出数据
            // 4. 再去缓存到redis
            stringRedisTemplate.opsForValue().set(Constants.FILES_KEY, JSONUtil.toJsonStr(files));
        } else {
            // 减轻数据库的压力
            // 5. 如果有, 从redis缓存中获取数据
            files = JSONUtil.toBean(jsonStr, new TypeReference<List<Files>>() {
            }, true);
        }
        return Result.success(files);
    }
}
