package com.yx.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yx.seckill.entity.Order;
import com.yx.seckill.entity.SeckillOrder;
import com.yx.seckill.entity.User;
import com.yx.seckill.service.GoodsService;
import com.yx.seckill.service.OrderService;
import com.yx.seckill.service.SeckillOrderService;
import com.yx.seckill.vo.GoodsVo;
import com.yx.seckill.vo.RespBean;
import com.yx.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private SeckillOrderService seckillOrderService;

    /**
     * 秒杀（静态化后的版本）
     * 返回JSON而不是页面跳转
     */
    @RequestMapping(value = "/doSeckill", method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSeckill(User user, Long goodsId) {
        // 1. 未登录校验
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }

        // 2. 查询商品
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        if (goods == null) {
            return RespBean.error(RespBeanEnum.GOODS_NOT_EXIST);
        }

        // 3. 库存检查
        if (goods.getStockCount() <= 0) {
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        // 4. 是否重复秒杀
        SeckillOrder seckillOrder = seckillOrderService
                .getOne(new QueryWrapper<SeckillOrder>()
                        .eq("user_id", user.getId())
                        .eq("goods_id", goodsId));
        if (seckillOrder != null) {
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }

        // 5. 执行秒杀
        Order order = orderService.seckill(user, goods);

        // 6. 返回订单信息
        return RespBean.success(order);
    }

    // /**
    //  * 旧版本秒杀（保留但标记为过时）
    //  */
    // @RequestMapping("/doSeckill2")
    // @Deprecated
    // public String doSeckill2(Model model, User user, Long goodsId) {
    //     if (user == null) {
    //         return "redirect:/login/toLogin";
    //     }
    //     model.addAttribute("user", user);
    //
    //     GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
    //     if (goods.getStockCount() <= 0) {
    //         model.addAttribute("errorMsg", "库存不足！");
    //         return "secKillFail";
    //     }
    //
    //     SeckillOrder seckillOrder = seckillOrderService
    //             .getOne(new QueryWrapper<SeckillOrder>()
    //                     .eq("user_id", user.getId())
    //                     .eq("goods_id", goodsId));
    //     if (seckillOrder != null) {
    //         model.addAttribute("errorMsg", "每人限购一件，不能重复秒杀！");
    //         return "secKillFail";
    //     }
    //
    //     Order order = orderService.seckill(user, goods);
    //     model.addAttribute("order", order);
    //     model.addAttribute("goods", goods);
    //
    //     return "orderDetail";
    // }
}