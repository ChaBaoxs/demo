package com.jc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jc.entity.Order;
import com.jc.entity.Plan;
import com.jc.mapper.OrderMapper;
import com.jc.mapper.StuCourseMapper;
import com.jc.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Plan findStatus(Long id) {
        return orderMapper.findStatus(id);
    }

    @Override
    public Order allPrice() {
        return orderMapper.allPrice();
    }
}
