package com.yx.seckill.controller;

import com.yx.seckill.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @GetMapping("/toList")
    public String toList(HttpServletRequest request,
                         Model model,
                         @CookieValue(value = "userTicket", required = false) String ticket) {
        // 1. 若 cookie 为空 → 重定向到登录页
        if (ObjectUtils.isEmpty(ticket)) {
            return "redirect:/login/toLogin";
        }
        // 2. 从 Session 读取 user
        User user = (User) request.getSession().getAttribute(ticket);
        if (user == null) {
            return "redirect:/login/toLogin";
        }
        // 3. 写入 model，页面可展示用户昵称
        model.addAttribute("user", user);
        return "goodsList";   // thymeleaf 模板
    }
}