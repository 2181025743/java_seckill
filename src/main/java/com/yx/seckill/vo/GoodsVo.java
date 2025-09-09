package com.yx.seckill.vo;

import com.yx.seckill.entity.Goods;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true) // 建议添加
public class GoodsVo extends Goods {
    private BigDecimal seckillPrice;  // 秒杀价
    private Integer stockCount;       // 秒杀库存
    private LocalDateTime startDate;  // <-- 改为 LocalDateTime
    private LocalDateTime endDate;    // <-- 改为 LocalDateTime
}