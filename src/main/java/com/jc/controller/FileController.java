package com.jc.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jc.common.Constants;
import com.jc.common.Result;
import com.jc.entity.Employee;
import com.jc.entity.Files;
import com.jc.exception.ServiceException;
import com.jc.mapper.FileMapper;
import com.jc.service.FileService;
import com.jc.untils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
//import sun.reflect.misc.FieldUtil;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 文件上传接口
 */
@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Resource
    private FileService fileService;

    @Resource
    private FileMapper fileMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

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
//        String type1 = file.getContentType();
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
        Files dbFiles = getFileByMd5(md5);
        if (dbFiles != null) {
            url = dbFiles.getUrl();
        } else {
            // 上传文件到磁盘
            file.transferTo(uploadFile);
            // 数据库若不存在重复文件，则不删除刚才上传的文件
            url = "http://localhost:9090/file/" + fileUUID;
        }

        //存储数据库
        Files saveFile = new Files();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size/1024); // 单位 kb
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        fileService.save(saveFile);

//        // 从redis取出数据，操作完，再设置（不用查询数据库）
//        String json = stringRedisTemplate.opsForValue().get(Constants.FILES_KEY);
//        List<Files> files1 = JSONUtil.toBean(json, new TypeReference<List<Files>>() {
//        }, true);
//        files1.add(saveFile);
//        setCache(Constants.FILES_KEY, JSONUtil.toJsonStr(files1));

        // 从数据库查出数据
        List<Files> files = fileMapper.selectList(null);
        // 设置最新的缓存
        setCache(Constants.FILES_KEY, JSONUtil.toJsonStr(files));

        // 最简单的方式：直接清空缓存
//        flushRedis(Constants.FILES_KEY);

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
     * 通过文件的md5查询文件
     * @param md5
     * @return
     */
    private Files getFileByMd5(String md5) {
        // 查询文件的md5是否存在
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        List<Files> filesList = fileService.list(queryWrapper);
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
        LambdaQueryWrapper<Files> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        //StringUtils.isNotEmpty(name)类似if(name!= null)
        queryWrapper.like(StringUtils.isNotEmpty(name),Files::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Files::getCreateTime);
        //执行查询
        fileService.page(page,queryWrapper);

        //获取当前用户信息
//        Employee currentUser = TokenUtils.getCurrentUser();
//        System.out.println("获取当前用户信息========="+currentUser.getUsername());

        return Result.success(fileService.page(page,queryWrapper));
    }

    /**
     * 删除图片信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        fileService.removeById(id);
        flushRedis(Constants.FILES_KEY);
        return Result.success("图片信息删除成功");
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @DeleteMapping("/del/batch")
    public Result delete(@RequestBody List<Integer> ids){
        fileService.removeByIds(ids);
        flushRedis(Constants.FILES_KEY);
        return Result.success("图片信息批量删除成功");
    }

    /**
     * 根据id修改图片信息
     * @param files
     * @return
     */
    @PostMapping("/update")
    public Result update(@RequestBody Files files){
        log.info("修改图片，图片信息========={}",files.toString());
        return Result.success(fileService.updateById(files));
    }

    @PostMapping("/down")
    public Result down(@RequestBody Files files){
        if (!files.getEnable()){
            throw new ServiceException(Constants.CODE_600, "文件已禁用");
        }
        return Result.success("图片下载获取成功");
    }

    // 设置缓存
    private void setCache(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    // 删除缓存
    private void flushRedis(String key) {
        stringRedisTemplate.delete(key);
    }
}
