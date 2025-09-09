package com.yx.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yx.seckill.entity.Goods;
import com.yx.seckill.vo.GoodsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author yx
 * @since 2025-09-09
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    /**
     * 获取商品列表（包含秒杀信息）
     *
     * @return 商品列表
     */
    List<GoodsVo> findGoodsVo();

    /**
     * 根据商品ID获取商品详情（包含秒杀信息）
     *
     * @param goodsId 商品ID
     * @return 商品详情
     */
    GoodsVo findGoodsVoByGoodsId(@Param("goodsId") Long goodsId);
}