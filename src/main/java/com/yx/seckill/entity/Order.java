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

@Getter
@Setter
@TableName("t_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("goods_id")
    private Long goodsId;

    @TableField("delivery_addr_id")
    private Long deliveryAddrId;

    @TableField("goods_name")
    private String goodsName;

    @TableField("goods_count")
    private Integer goodsCount;

    @TableField("goods_price")
    private BigDecimal goodsPrice;

    @TableField("order_channel")
    private Byte orderChannel;

    @TableField("status")
    private Byte status;

    @TableField("create_date")
    private LocalDateTime createDate;

    @TableField("pay_date")
    private LocalDateTime payDate;
}
