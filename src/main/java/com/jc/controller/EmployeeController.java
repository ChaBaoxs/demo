package com.jc.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jc.common.Constants;
import com.jc.common.Result;
import com.jc.controller.dto.EmployeeDTO;
import com.jc.controller.dto.PasswordDTO;
import com.jc.controller.dto.StudentDTO;
import com.jc.controller.dto.TeacherDTO;
import com.jc.entity.Course;
import com.jc.entity.Employee;
import com.jc.entity.Order;
import com.jc.entity.StuCourse;
import com.jc.service.CourseService;
import com.jc.service.EmployeeService;
import com.jc.service.StuCourseService;
import com.jc.untils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StuCourseService stuCourseService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping
    public Result findAll() {
        return Result.success(employeeService.list());
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam int pageNum,@RequestParam int pageSize,@RequestParam String username) {
//        log.info("page={},pageSize={},username={}",pageNum,pageSize,username);

        //构造分页构造器
        Page page = new Page(pageNum,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        //StringUtils.isNotEmpty(name)类似if(name!= null)
        queryWrapper.like(StringUtils.isNotEmpty(username),Employee::getUsername,username);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getCreateTime);
        //执行查询
        employeeService.page(page,queryWrapper);

        //获取当前用户信息
        Employee currentUser = TokenUtils.getCurrentUser();
        System.out.println("获取当前用户信息========="+currentUser.getUsername());

//        //1.从缓存获取数据
//        String jsonStr = stringRedisTemplate.opsForValue().get(Constants.EMPLOYEE_KEY);
//        Page page1;
//        if (StrUtil.isBlank(jsonStr)){//2.取出来的json数据为空
//            page1 = employeeService.page(page, queryWrapper);//3.从数据库取出数据
//            //4.再去缓存到redis
//            stringRedisTemplate.opsForValue().set(Constants.EMPLOYEE_KEY,JSONUtil.toJsonStr(page1));
//        }else {
//            //减轻数据库压力
//            //5.如果有，从redis缓存中获取数据
//            page1 = JSONUtil.toBean(jsonStr, new TypeReference<Page>() {
//            }, true);
//        }
//
//        return Result.success(page1);
        return Result.success(employeeService.page(page, queryWrapper));
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping
    public Result save( @RequestBody Employee employee){
        log.info("新增员工，员工信息========={}",employee.toString());
        return Result.success( employeeService.saveAll(employee));
    }

    /**
     * 根据id修改员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public Result update(@RequestBody Employee employee){
        log.info("修改员工，员工信息========={}",employee.toString());
        return Result.success(employeeService.updateWithId(employee));
    }

    /**
     * 删除员工信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id){
        employeeService.removeById(id);
        return Result.success("员工信息删除成功");
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @DeleteMapping("/del/batch")
    public Result delete(@RequestBody List<Long> ids){
        employeeService.removeByIds(ids);
        return Result.success("员工信息删除成功");
    }

    /**
     * 登录
     * @param employeeDTO
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody EmployeeDTO employeeDTO){
        EmployeeDTO dto = employeeService.login(employeeDTO);
        return Result.success(dto);
    }

    /**
     * 根据id获取用户信息
     * @param id
     * @return
     */
    @GetMapping("/id/{id}")
    public Result findById(@PathVariable Long id){
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        System.out.println("**************************************************根据id获取用户信息");
        return Result.success(employeeService.getOne(queryWrapper));
    }

    /**
     * 查询学生总数
     * @return
     */
    @GetMapping("/total")
    public Result findUserTotal(){
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role","ROLE_STUDENT" );
        return Result.success(employeeService.count(queryWrapper));
    }

    /**
     * 根据职位查询信息
     * @param role
     * @return
     */
    @GetMapping("/role/{role}")
    public Result findByRole(@PathVariable String role) {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role",role);
        List<Employee> list = employeeService.list(queryWrapper);
        return Result.success(list);
    }

    /**
     * 查询教师所授课程
     * @param pageNum
     * @param pageSize
     * @param username
     * @return
     */
    @GetMapping("/page/teacher")
    public Result findTeacher(@RequestParam int pageNum,@RequestParam int pageSize,@RequestParam String username) {
        //构造分页构造器
        Page page = new Page(pageNum,pageSize);
        Page<TeacherDTO> teacherDTOPage = new Page<>();
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        //StringUtils.isNotEmpty(name)类似if(name!= null)
        queryWrapper.like(StringUtils.isNotEmpty(username),Employee::getUsername,username);
        queryWrapper.like(Employee::getRole,"ROLE_TEACHER");
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getCreateTime);
        //执行查询
        employeeService.page(page,queryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(page,teacherDTOPage,"records");

        List<Employee> records = page.getRecords();
        //将records处理,item代表employee,遍历出来的每一个教师对象
        //需要返回一个TeacherDTO类型对象
        List<TeacherDTO> list = records.stream().map((item)->{
            TeacherDTO teacherDTO = new TeacherDTO();
            //new出来的DTO其他值为空，需要拷贝
            BeanUtils.copyProperties(item,teacherDTO);
            Long teacherId = item.getId();//教师id
            //查询当前教师课程信息，从course表查询
            LambdaQueryWrapper<Course> courseQueryWrapper = new LambdaQueryWrapper();
            courseQueryWrapper.eq(Course::getTeacherId,teacherId);
            List<Course> courseList = courseService.list(courseQueryWrapper);
            teacherDTO.setCourses(courseList);
            return teacherDTO;
        }).collect(Collectors.toList());//遍历后需要收集转成集合
        teacherDTOPage.setRecords(list);
        return Result.success(teacherDTOPage);
    }

    /**
     * 查询学生与学生计划
     * @param pageNum
     * @param pageSize
     * @param username
     * @return
     */
    @GetMapping("/page/student")
    public Result findStudent(@RequestParam int pageNum,@RequestParam int pageSize,
                              @RequestParam String username,@RequestParam Integer isDelete,
                              @RequestParam String planName) {
        Page<StudentDTO> page = employeeService.findPage(new Page<>(pageNum, pageSize), username,isDelete,planName);
        return Result.success(page);
    }


    /**
     * 查询学生选课信息
     * @param id
     * @return
     */
    @GetMapping("/course/{id}")
    public Result findStudentCourse(@PathVariable Long id){
        List<StuCourse> stuCourses =stuCourseService.findStuCourse(id);
        return Result.success(stuCourses);
    }

    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Employee> list = employeeService.list();
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
//        writer.addHeaderAlias("id", "用户ID");
//        writer.addHeaderAlias("username", "用户名");
//        writer.addHeaderAlias("password", "密码");
//        writer.addHeaderAlias("name", "昵称");
//        writer.addHeaderAlias("sex", "性别");
//        writer.addHeaderAlias("email", "邮箱");
//        writer.addHeaderAlias("phone", "电话");
//        writer.addHeaderAlias("idNumber", "身份证号码");
////        writer.addHeaderAlias("address", "地址");
//        writer.addHeaderAlias("avatarUrl", "头像");
//        writer.addHeaderAlias("role", "职位");
//        writer.addHeaderAlias("status", "状态");
//        writer.addHeaderAlias("createTime", "创建时间");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

    }

    /**
     * excel 导入
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
        List<Employee> list = reader.readAll(Employee.class);

//        // 方式2：忽略表头的中文，直接读取表的内容
//        List<List<Object>> list = reader.read(1);
//        List<User> users = CollUtil.newArrayList();
//        for (List<Object> row : list) {
//            User user = new User();
//            user.setUsername(row.get(0).toString());
//            user.setPassword(row.get(1).toString());
//            user.setNickname(row.get(2).toString());
//            user.setEmail(row.get(3).toString());
//            user.setPhone(row.get(4).toString());
//            user.setAddress(row.get(5).toString());
//            user.setAvatarUrl(row.get(6).toString());
//            users.add(user);
//        }
//        System.out.println("*********************:"+list);
        employeeService.saveBatch(list);
        return Result.success(true);
    }

    /**
     * 注册
     * @param employeeDTO
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody EmployeeDTO employeeDTO) {
        String username = employeeDTO.getUsername();
        String password = employeeDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        return Result.success(employeeService.register(employeeDTO));
    }

    /**
     * 修改密码
     * @param passwordDTO
     * @return
     */
    @PostMapping("/password")
    public Result password(@RequestBody PasswordDTO passwordDTO) {
        passwordDTO.setPassword(SecureUtil.md5(passwordDTO.getPassword()));
        passwordDTO.setNewPassword(SecureUtil.md5(passwordDTO.getNewPassword()));
        employeeService.updatePassword(passwordDTO);
        return Result.success();
    }
}
