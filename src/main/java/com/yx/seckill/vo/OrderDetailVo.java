package com.yx.seckill.vo;

import com.yx.seckill.entity.Order;
import lombok.Data;

/**
 * 订单详情VO
 */
@Data
public class OrderDetailVo {
    private Order order;
    private GoodsVo goods;
}