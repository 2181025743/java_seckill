package com.yx.seckill.service.impl;

import com.yx.seckill.entity.Order;
import com.yx.seckill.mapper.OrderMapper;
import com.yx.seckill.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author yx
 * @since 2025-09-09
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

}
