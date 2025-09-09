package com.yx.seckill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

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

    /**
     * 商品ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品名称
     */
    @TableField("goods_name")
    private String goodsName;

    /**
     * 商品标题
     */
    @TableField("goods_title")
    private String goodsTitle;

    /**
     * 商品图片
     */
    @TableField("goods_img")
    private String goodsImg;

    /**
     * 商品详情
     */
    @TableField("goods_detail")
    private String goodsDetail;

    /**
     * 商品单价
     */
    @TableField("goods_price")
    private BigDecimal goodsPrice;

    /**
     * 库存（-1 表示无限制，其他为具体库存数）
     */
    @TableField("goods_stock")
    private Integer goodsStock;
}
