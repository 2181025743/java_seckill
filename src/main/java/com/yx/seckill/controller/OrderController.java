package com.yx.seckill.controller;

import com.yx.seckill.entity.Order;
import com.yx.seckill.entity.User;
import com.yx.seckill.service.GoodsService;
import com.yx.seckill.service.OrderService;
import com.yx.seckill.vo.GoodsVo;
import com.yx.seckill.vo.OrderDetailVo;
import com.yx.seckill.vo.RespBean;
import com.yx.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private GoodsService goodsService;

    /**
     * 订单详情接口（静态化）
     */
    @RequestMapping("/detail/{orderId}")
    @ResponseBody
    public RespBean detail(User user, @PathVariable("orderId") Long orderId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }

        // 查询订单
        Order order = orderService.getById(orderId);
        if (order == null) {
            return RespBean.error(RespBeanEnum.ORDER_NOT_EXIST);
        }

        // 验证订单是否属于当前用户
        if (!order.getUserId().equals(user.getId())) {
            return RespBean.error(RespBeanEnum.REQUEST_ILLEGAL);
        }

        // 查询商品信息
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(order.getGoodsId());

        // 封装返回数据
        OrderDetailVo detailVo = new OrderDetailVo();
        detailVo.setOrder(order);
        detailVo.setGoods(goods);

        return RespBean.success(detailVo);
    }
}