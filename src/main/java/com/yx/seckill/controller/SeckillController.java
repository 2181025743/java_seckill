package com.yx.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yx.seckill.entity.Order;
import com.yx.seckill.entity.SeckillOrder;
import com.yx.seckill.entity.User;
import com.yx.seckill.service.GoodsService;
import com.yx.seckill.service.OrderService;
import com.yx.seckill.service.SeckillOrderService;
import com.yx.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private SeckillOrderService seckillOrderService;

    @RequestMapping("/doSeckill")
    public String doSeckill(Model model, User user, Long goodsId) {
        // 1. 未登录校验
        if (user == null) {
            return "redirect:/login/toLogin";
        }
        model.addAttribute("user", user);

        // 2. 查询商品
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);

        // 3. 库存检查
        if (goods.getStockCount() <= 0) {
            model.addAttribute("errorMsg", "库存不足！");
            return "seckillFail";
        }

        // 4. 是否重复秒杀（根据 userId+goodsId 在秒杀订单表里查）
        SeckillOrder seckillOrder = seckillOrderService
                .getOne(new QueryWrapper<SeckillOrder>()
                        .eq("user_id", user.getId())
                        .eq("goods_id", goodsId));
        if (seckillOrder != null) {
            model.addAttribute("errorMsg", "每人限购一件，不能重复秒杀！");
            return "secKillFail";
        }

        // 5. 正式执行秒杀：减库存、生成订单和秒杀订单
        Order order = orderService.seckill(user, goods);
        model.addAttribute("order", order);
        model.addAttribute("goods", goods);

        return "orderDetail"; // 跳转订单详情页
    }
}