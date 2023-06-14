package com.jc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jc.entity.Files;
import com.jc.mapper.FileMapper;
import com.jc.service.FileService;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, Files> implements FileService {
}
