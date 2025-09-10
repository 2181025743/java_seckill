package com.yx.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yx.seckill.entity.Order;
import com.yx.seckill.entity.User;
import com.yx.seckill.vo.GoodsVo;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author yx
 * @since 2025-09-09
 */
public interface OrderService extends IService<Order> {

    Order seckill(User user, GoodsVo goods);
}
