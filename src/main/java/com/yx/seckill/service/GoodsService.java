package com.yx.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yx.seckill.entity.Goods;
import com.yx.seckill.vo.GoodsVo;

import java.util.List;

public interface GoodsService extends IService<Goods> {

    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
