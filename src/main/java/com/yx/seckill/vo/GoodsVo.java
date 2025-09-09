package com.yx.seckill.vo;

import com.yx.seckill.entity.Goods;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class GoodsVo extends Goods {
    private BigDecimal seckillPrice;  // 秒杀价
    private Integer stockCount;       // 秒杀库存
    private Date startDate;           // 秒杀开始时间
    private Date endDate;             // 秒杀结束时间
}
