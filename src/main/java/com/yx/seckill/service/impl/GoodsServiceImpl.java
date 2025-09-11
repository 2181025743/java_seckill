package com.yx.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yx.seckill.entity.Goods;
import com.yx.seckill.mapper.GoodsMapper;
import com.yx.seckill.service.GoodsService;
import com.yx.seckill.vo.GoodsVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author yx
 * @since 2025-09-09
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Resource
    private GoodsMapper goodsMapper;

    /**
     * 获取商品列表（包含秒杀信息）
     */
    public List<GoodsVo> findGoodsVo() {
        return goodsMapper.findGoodsVo();
    }// GoodsServiceImpl.java

    public GoodsVo findGoodsVoByGoodsId(Long goodsId) {
        return goodsMapper.findGoodsVoByGoodsId(goodsId);
    }
}
