package com.jc.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jc.common.Constants;
import com.jc.common.Result;
import com.jc.controller.dto.TeacherDTO;
import com.jc.controller.dto.VideoDTO;
import com.jc.entity.*;
import com.jc.exception.ServiceException;
import com.jc.service.CourseService;
import com.jc.service.VideoService;
import com.jc.untils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件上传接口
 */
@Slf4j
@RestController
@RequestMapping("/videoFile")
public class VideoController {

    @Value("${videoFiles.upload.path}")
    private String fileUploadPath;

    @Value("${videoFiles.upload.coverPath}")
    private String coverUploadPath;

    @Resource
    private VideoService videoService;

    @Resource
    private CourseService courseService;


    /**
     * 文件上传接口
     * @param file 前端传输过来的文件
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        //获取原始名称
        String originalFilename = file.getOriginalFilename();
        //获取文件类型
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();

        // 定义一个文件唯一的标识码
        String fileUUID = IdUtil.fastSimpleUUID() + StrUtil.DOT + type;
        //先存储到磁盘
        File uploadFile = new File(fileUploadPath + fileUUID);
        // 判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
        File parentFile = uploadFile.getParentFile();
        if(!parentFile.exists()) {
            parentFile.mkdirs();
        }

        String url;
        // 获取文件的md5
        String md5 = SecureUtil.md5(file.getInputStream());
        // 从数据库查询是否存在相同的记录
        Video dbFiles = getFileByMd5(md5);
        if (dbFiles != null) {
            url = dbFiles.getUrl();
        } else {
            // 上传文件到磁盘
            file.transferTo(uploadFile);
            // 数据库若不存在重复文件，则不删除刚才上传的文件
            url = "http://localhost:9090/videoFile/" + fileUUID;
        }

        // 存储数据库
        Video saveFile = new Video();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size/1024); // 单位 kb
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        videoService.save(saveFile);

        return url;

    }

    /**
     * 视频封面上传接口
     * @param file 前端传输过来的文件
     * @return
     * @throws IOException
     */
    @PostMapping("/cover/upload")
    public String coverUpload(@RequestParam MultipartFile file) throws IOException {
        //获取原始名称
        String originalFilename = file.getOriginalFilename();
        //获取文件类型
        String type = FileUtil.extName(originalFilename);

        // 定义一个文件唯一的标识码
        String fileUUID = IdUtil.fastSimpleUUID() + StrUtil.DOT + type;
        //先存储到磁盘
        File uploadFile = new File(coverUploadPath + fileUUID);
        // 判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
        File parentFile = uploadFile.getParentFile();
        if(!parentFile.exists()) {
            parentFile.mkdirs();
        }

        String url;
        // 获取文件的md5
        String md5 = SecureUtil.md5(file.getInputStream());
        // 从数据库查询是否存在相同的记录
        Video dbFiles = getFileByMd5(md5);
        if (dbFiles != null) {
            url = dbFiles.getUrl();
        } else {
            // 上传文件到磁盘
            file.transferTo(uploadFile);
            // 数据库若不存在重复文件，则不删除刚才上传的文件
            url = "http://localhost:9090/videoFile/cover/" + fileUUID;
        }

//        // 存储数据库
//        Video saveFile = new Video();
//        saveFile.setCoverUrl(url);
//        videoService.save(saveFile);
        return url;

    }

    /**
     * 文件下载接口   http://localhost:9090/file/{fileUUID}
     * @param fileUUID
     * @param response
     * @throws IOException
     */
    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {


        // 根据文件的唯一标识码获取文件
        File uploadFile = new File(fileUploadPath + fileUUID);
        // 设置输出流的格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUUID, "UTF-8"));
        response.setContentType("application/octet-stream");

        // 读取文件的字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }

    /**
     * 头像文件下载接口   http://localhost:9090/file/{fileUUID}
     * @param fileUUID
     * @param response
     * @throws IOException
     */
    @GetMapping("/cover/{fileUUID}")
    public void downloadCover(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {


        // 根据文件的唯一标识码获取文件
        File uploadFile = new File(coverUploadPath + fileUUID);
        // 设置输出流的格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUUID, "UTF-8"));
        response.setContentType("application/octet-stream");

        // 读取文件的字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }

    /**
     * 通过文件的md5查询文件
     * @param md5
     * @return
     */
    private Video getFileByMd5(String md5) {
        // 查询文件的md5是否存在
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        List<Video> filesList = videoService.list(queryWrapper);
        return filesList.size() == 0 ? null : filesList.get(0);
    }

    /**
     * 分页查询接口
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam int pageNum, @RequestParam int pageSize, @RequestParam String name) {

        //构造分页构造器
        Page page = new Page(pageNum,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        //StringUtils.isNotEmpty(name)类似if(name!= null)
        queryWrapper.like(StringUtils.isNotEmpty(name),Video::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Video::getCreateTime);
        //获取当前用户信息
        Employee currentUser = TokenUtils.getCurrentUser();
        String role = currentUser.getRole();
//        if (!role.equals("ROLE_ADMIN") ){
//            Long id = currentUser.getId();
//            queryWrapper.eq("teacher_id",id);
//        }
        //执行查询
        videoService.page(page,queryWrapper);

        Page<VideoDTO> videoDTOPage = new Page<>();
        //对象拷贝
        BeanUtils.copyProperties(page,videoDTOPage,"records");
        List<Video> records = page.getRecords();
        //将records处理,item代表video,遍历出来的每一个教师对象
        //需要返回一个VideoDTO类型对象
        List<VideoDTO> list = records.stream().map((item)->{
            VideoDTO videoDTO = new VideoDTO();
            //new出来的DTO其他值为空，需要拷贝
            BeanUtils.copyProperties(item,videoDTO);
            Integer courseId = item.getCourseId();
            //查询当前课程信息，从course表查询
            QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<>();
            courseQueryWrapper.eq("id",courseId);
            courseQueryWrapper.select("name","teacher_id");
            Course course = courseService.getOne(courseQueryWrapper);
            videoDTO.setCName(course.getName());
            videoDTO.setTid(course.getTeacherId());
            return videoDTO;
        }).collect(Collectors.toList());//遍历后需要收集转成集合
        videoDTOPage.setRecords(list);

        return Result.success(videoDTOPage);
    }

    /**
     * 根据id查询所有
     * @return
     */
    @GetMapping("/findAll/{id}")
    public Result findById(@PathVariable Long id){
        List<VideoDTO> videoDTOList =videoService.findById(id);
        return Result.success(videoDTOList);
    }

    /**
     * 根据id查询所有
     * @return
     */
    @GetMapping("/findAll")
    public Result findAll(@RequestParam(defaultValue = "") String name){
        //构造条件构造器
        LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        //StringUtils.isNotEmpty(name)类似if(name!= null)
        queryWrapper.like(StringUtils.isNotEmpty(name),Video::getName,name);
        List<Video> videoList =videoService.list(queryWrapper);
        return Result.success(videoList);
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    public Result getById(@PathVariable Integer id) {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        return Result.success(videoService.getOne(queryWrapper));
    }

    /**
     * 删除图片信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        videoService.removeById(id);
        return Result.success("图片信息删除成功");
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @DeleteMapping("/del/batch")
    public Result delete(@RequestBody List<Integer> ids){
        videoService.removeByIds(ids);
        return Result.success("图片信息批量删除成功");
    }

    /**
     * 根据id修改图片信息
     * @param files
     * @return
     */
    @PostMapping("/update")
    public Result update(@RequestBody Video files){
        log.info("修改图片，图片信息========={}",files.toString());
        return Result.success(videoService.saveOrUpdate(files));
    }

    @PostMapping("/down")
    public Result down(@RequestBody Video files){
        if (!files.getEnable()){
            throw new ServiceException(Constants.CODE_600, "文件已禁用");
        }
        return Result.success("图片下载获取成功");
    }

    /**
     * 课程名称信息
     * @return
     */
    @GetMapping("/course")
    public Result getIcons() {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        //获取当前用户信息
        Employee currentUser = TokenUtils.getCurrentUser();
        Long id =currentUser.getId();
        String role = currentUser.getRole();
        if (role.equals("ROLE_TEACHER") ){
            queryWrapper.eq("teacher_id",id);
        }
        return Result.success(courseService.list(queryWrapper));
    }
}
