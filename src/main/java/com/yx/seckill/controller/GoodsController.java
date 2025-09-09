package com.yx.seckill.controller;

import com.yx.seckill.entity.User;
import com.yx.seckill.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/toList")
    public String toList(Model model, User user) {
        // 1. 若 cookie 为空 → 重定向到登录页
        if (user == null) {
            return "redirect:/login/toLogin";
        }
        // 3. 写入 model，页面可展示用户昵称
        model.addAttribute("user", user);
        return "goodsList";   // thymeleaf模板
    }
}