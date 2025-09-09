package com.yx.seckill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 商品表
 * </p>
 *
 * @author yx
 * @since 2025-09-09
 */
@Getter
@Setter
@TableName("t_goods")
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("goods_name")
    private String goodsName;
    @TableField("goods_title")
    private String goodsTitle;
    @TableField("goods_img")
    private String goodsImg;
    @TableField("goods_detail")
    private String goodsDetail;
    @TableField("goods_price")
    private BigDecimal goodsPrice;
    @TableField("goods_stock")
    private Integer goodsStock;
}
