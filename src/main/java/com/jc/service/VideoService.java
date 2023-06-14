package com.jc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jc.controller.dto.VideoDTO;
import com.jc.entity.Video;

import java.util.List;

public interface VideoService extends IService<Video> {
    List<VideoDTO> findById(Long id);

    List<VideoDTO> findAll();
}
