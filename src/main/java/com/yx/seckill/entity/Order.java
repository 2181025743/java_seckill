package com.yx.seckill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author yx
 * @since 2025-09-09
 */
@Getter
@Setter
@TableName("t_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 商品ID
     */
    @TableField("goods_id")
    private Long goodsId;

    /**
     * 收货地址ID
     */
    @TableField("delivery_addr_id")
    private Long deliveryAddrId;

    /**
     * 商品名称（冗余）
     */
    @TableField("goods_name")
    private String goodsName;

    /**
     * 商品数量
     */
    @TableField("goods_count")
    private Integer goodsCount;

    /**
     * 商品单价
     */
    @TableField("goods_price")
    private BigDecimal goodsPrice;

    /**
     * 下单渠道：1=PC，2=Android，3=iOS
     */
    @TableField("order_channel")
    private Byte orderChannel;

    /**
     * 订单状态：0=新建未支付，1=已支付，2=已发货，3=已收货，4=已退款，5=已完成
     */
    @TableField("status")
    private Byte status;

    /**
     * 订单创建时间
     */
    @TableField("create_date")
    private LocalDateTime createDate;

    /**
     * 订单支付时间
     */
    @TableField("pay_date")
    private LocalDateTime payDate;
}
