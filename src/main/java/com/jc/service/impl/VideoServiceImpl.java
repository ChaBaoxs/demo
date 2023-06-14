package com.jc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jc.controller.dto.VideoDTO;
import com.jc.entity.Video;
import com.jc.mapper.StuCourseMapper;
import com.jc.mapper.VideoMapper;
import com.jc.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    @Autowired
    private VideoMapper videoMapper;

    @Override
    public List<VideoDTO> findById(Long id) {
        return videoMapper.findById(id);
    }

    @Override
    public List<VideoDTO> findAll() {
        return videoMapper.findAll();
    }
}
