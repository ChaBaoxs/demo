package com.jc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jc.controller.dto.VideoDTO;
import com.jc.entity.Video;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VideoMapper extends BaseMapper<Video> {
    List<VideoDTO> findById(@Param("id") Long id);

    List<VideoDTO> findAll();
}
