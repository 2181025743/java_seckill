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
 * 秒杀商品表
 * </p>
 *
 * @author yx
 * @since 2025-09-09
 */
@Getter
@Setter
@TableName("t_seckill_goods")
public class SeckillGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 秒杀商品ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品ID，关联t_goods
     */
    @TableField("goods_id")
    private Long goodsId;

    /**
     * 秒杀价
     */
    @TableField("seckill_price")
    private BigDecimal seckillPrice;

    /**
     * 秒杀库存
     */
    @TableField("stock_count")
    private Integer stockCount;

    /**
     * 秒杀开始时间
     */
    @TableField("start_date")
    private LocalDateTime startDate;

    /**
     * 秒杀结束时间
     */
    @TableField("end_date")
    private LocalDateTime endDate;
}
