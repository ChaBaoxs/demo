package com.jc.controller.dto;

import com.jc.entity.Course;
import com.jc.entity.Video;
import lombok.Data;

@Data
public class VideoDTO extends Video {

    private String cName;

    private Long tid;

}
