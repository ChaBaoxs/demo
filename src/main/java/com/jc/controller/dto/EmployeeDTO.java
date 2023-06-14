package com.jc.controller.dto;

import com.jc.entity.Menu;
import lombok.Data;

import java.util.List;

/**
 * 接受前端登录请求的参数
 */
@Data
public class EmployeeDTO {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String avatarUrl;
    private String token;
    private String role;
    private List<Menu> menus;
}
