package com.jc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jc.entity.Order;
import com.jc.entity.Plan;

public interface OrderService extends IService<Order> {
    Plan findStatus(Long id);

    Order allPrice();
}
