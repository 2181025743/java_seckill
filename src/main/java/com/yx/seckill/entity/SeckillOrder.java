package com.yx.seckill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 秒杀订单表
 * </p>
 *
 * @author yx
 * @since 2025-09-09
 */
@Getter
@Setter
@TableName("t_seckill_order")
public class SeckillOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 订单ID，对应t_order
     */
    @TableField("order_id")
    private Long orderId;

    /**
     * 商品ID
     */
    @TableField("goods_id")
    private Long goodsId;
}
