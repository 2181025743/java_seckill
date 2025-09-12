package com.yx.seckill.vo;

import com.yx.seckill.entity.User;
import lombok.Data;

/**
 * 商品详情页数据VO
 */
@Data
public class DetailVo {
    private User user;
    private GoodsVo goods;
    private int seckillStatus;  // 0-未开始，1-进行中，2-已结束
    private long remainSeconds; // 剩余秒数
}