package com.yx.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yx.seckill.entity.Order;
import com.yx.seckill.entity.SeckillGoods;
import com.yx.seckill.entity.SeckillOrder;
import com.yx.seckill.entity.User;
import com.yx.seckill.mapper.OrderMapper;
import com.yx.seckill.mapper.SeckillGoodsMapper;
import com.yx.seckill.mapper.SeckillOrderMapper;
import com.yx.seckill.service.OrderService;
import com.yx.seckill.vo.GoodsVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    @Resource
    private SeckillGoodsMapper seckillGoodsMapper;

    @Resource
    private SeckillOrderMapper seckillOrderMapper;

    @Resource
    private OrderMapper orderMapper;

    @Override
    public Order seckill(User user, GoodsVo goods) {
        // 1. 减库存（只减秒杀表库存）
        SeckillGoods seckillGoods = seckillGoodsMapper
                .selectOne(new QueryWrapper<SeckillGoods>()
                        .eq("goods_id", goods.getId()));
        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
        seckillGoodsMapper.updateById(seckillGoods);

        // 2. 生成普通订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(goods.getSeckillPrice());
        order.setStatus((byte) 0); // 未支付
        order.setCreateDate(LocalDateTime.now());
        orderMapper.insert(order);

        // 3. 生成秒杀订单（关联普通订单）
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setGoodsId(goods.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrderMapper.insert(seckillOrder);

        return order;
    }
}
