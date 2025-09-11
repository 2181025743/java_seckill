package com.yx.seckill.controller;

import com.yx.seckill.entity.User;
import com.yx.seckill.service.impl.GoodsServiceImpl;
import com.yx.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsServiceImpl goodsService;

    @GetMapping("/toList")
    public String toList(Model model, User user) {
        // 1. 未登录 → 跳转登录
        if (user == null) {
            return "redirect:/login/toLogin";
        }

        // 2. 查询商品列表
        List<GoodsVo> goodsList = goodsService.findGoodsVo();

        // 3. 页面渲染参数
        model.addAttribute("user", user);
        model.addAttribute("goodsList", goodsList);

        return "goodsList";   // thymeleaf 模板
    }

    @RequestMapping("/toDetail/{id}")
    public String toDetail(Model model, User user,
                           @PathVariable("id") Long goodsId) {
        model.addAttribute("user", user);

        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);

        LocalDateTime startDate = goods.getStartDate();
        LocalDateTime endDate = goods.getEndDate();
        LocalDateTime nowDate = LocalDateTime.now();

        int seckillStatus = 0;
        long remainSeconds = 0;

        if (nowDate.isBefore(startDate)) {
            // 秒杀未开始
            seckillStatus = 0;
            remainSeconds = Duration.between(nowDate, startDate).toSeconds();
        } else if (nowDate.isAfter(endDate)) {
            // 秒杀已结束
            seckillStatus = 2;
            remainSeconds = -1;
        } else {
            // 秒杀进行中
            seckillStatus = 1;
            remainSeconds = 0;
        }

        model.addAttribute("seckillStatus", seckillStatus);
        model.addAttribute("remainSeconds", remainSeconds);

        return "goodsDetail";
    }
}