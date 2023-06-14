package com.jc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jc.entity.Menu;

import java.util.List;

public interface MenuService extends IService<Menu> {
    List<Menu> findMenus(String name);

//    List<Menu> findMenus(String name);
}
