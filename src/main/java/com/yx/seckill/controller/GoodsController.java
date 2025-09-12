package com.yx.seckill.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yx.seckill.entity.User;
import com.yx.seckill.service.GoodsService;
import com.yx.seckill.vo.DetailVo;
import com.yx.seckill.vo.GoodsVo;
import com.yx.seckill.vo.RespBean;
import com.yx.seckill.vo.RespBeanEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    private final GoodsService goodsService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ITemplateEngine templateEngine;
    private final ApplicationContext applicationContext;

    @Autowired
    public GoodsController(GoodsService goodsService,
                           RedisTemplate<String, Object> redisTemplate,
                           ITemplateEngine templateEngine,
                           ApplicationContext applicationContext) {
        this.goodsService = goodsService;
        this.redisTemplate = redisTemplate;
        this.templateEngine = templateEngine;
        this.applicationContext = applicationContext;
    }

    @GetMapping(value = "/toList", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model, User user,
                         HttpServletRequest request,
                         HttpServletResponse response) {

        // 1. 从Redis获取缓存
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsList");

        // 2. 如果缓存存在，直接返回
        if (!StringUtils.isEmpty(html)) {
            return html;
        }

        // 3. 未登录处理
        if (user == null) {
            return "redirect:/login/toLogin";
        }

        // 4. 查询商品列表数据
        List<GoodsVo> goodsList = goodsService.findGoodsVo();
        model.addAttribute("user", user);
        model.addAttribute("goodsList", goodsList);

        // 5. 创建 WebContext（新版本方式）
        WebContext context = createWebContext(request, response, model);

        // 6. 渲染模板
        html = templateEngine.process("goodsList", context);

        // 7. 存入Redis，设置60秒过期
        if (!StringUtils.isEmpty(html)) {
            valueOperations.set("goodsList", html, 60, TimeUnit.SECONDS);
        }

        return html;
    }

    // @RequestMapping(value = "/toDetail/{id}", produces = "text/html;charset=utf-8")
    // @ResponseBody
    // public String toDetail(Model model, User user,
    //                        @PathVariable("id") Long goodsId,
    //                        HttpServletRequest request,
    //                        HttpServletResponse response) {
    //
    //     // 1. 验证输入参数（防止XSS）
    //     if (!isValidGoodsId(goodsId)) {
    //         return "error/400"; // 返回错误页面
    //     }
    //
    //     // 2. 构建安全的缓存key
    //     String cacheKey = buildSafeCacheKey("goodsDetail", goodsId);
    //
    //     // 3. 从Redis获取缓存
    //     ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
    //     String html = (String) valueOperations.get(cacheKey);
    //
    //     // 4. 缓存存在则直接返回
    //     if (!StringUtils.isEmpty(html)) {
    //         return html;
    //     }
    //
    //     // 5. 添加用户信息到model
    //     model.addAttribute("user", user);
    //
    //     // 6. 查询商品详情数据
    //     GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
    //     if (goods == null) {
    //         return "error/404"; // 商品不存在
    //     }
    //     model.addAttribute("goods", goods);
    //
    //     // 7. 计算秒杀状态
    //     calculateSeckillStatus(goods, model);
    //
    //     // 8. 创建 WebContext（新版本方式）
    //     WebContext context = createWebContext(request, response, model);
    //
    //     // 9. 渲染模板
    //     html = templateEngine.process("goodsDetail", context);
    //
    //     // 10. 存入Redis
    //     if (!StringUtils.isEmpty(html)) {
    //         valueOperations.set(cacheKey, html, 60, TimeUnit.SECONDS);
    //     }
    //
    //     return html;
    // }
    @RequestMapping("/detail/{id}")
    @ResponseBody
    public RespBean detail(User user, @PathVariable("id") Long goodsId) {

        // 1. 参数校验
        if (!isValidGoodsId(goodsId)) {
            return RespBean.error(RespBeanEnum.REQUEST_ILLEGAL);
        }

        // 2. 查询商品详情
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        if (goods == null) {
            return RespBean.error(RespBeanEnum.GOODS_NOT_EXIST);
        }

        // 3. 计算秒杀状态
        LocalDateTime startDate = goods.getStartDate();
        LocalDateTime endDate = goods.getEndDate();
        LocalDateTime nowDate = LocalDateTime.now();

        int seckillStatus;
        long remainSeconds;

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

        // 4. 封装返回数据
        DetailVo detailVo = new DetailVo();
        detailVo.setUser(user);
        detailVo.setGoods(goods);
        detailVo.setSeckillStatus(seckillStatus);
        detailVo.setRemainSeconds(remainSeconds);

        // 5. 返回JSON数据
        return RespBean.success(detailVo);
    }

    /**
     * 创建 WebContext 的辅助方法（适配新版本 Thymeleaf）
     */
    private WebContext createWebContext(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Model model) {
        JakartaServletWebApplication application =
                JakartaServletWebApplication.buildApplication(request.getServletContext());

        IWebExchange webExchange =
                application.buildExchange(request, response);

        WebContext context = new WebContext(webExchange,
                RequestContextUtils.getLocale(request),
                model.asMap());

        return context;
    }

    /**
     * 验证商品ID的合法性（防止XSS攻击）
     */
    private boolean isValidGoodsId(Long goodsId) {
        // 检查是否为null
        if (goodsId == null) {
            return false;
        }

        // 检查范围（假设商品ID在合理范围内）
        if (goodsId <= 0 || goodsId > 1000000) {
            return false;
        }

        return true;
    }

    /**
     * 构建安全的缓存key（防止注入）
     */
    private String buildSafeCacheKey(String prefix, Long id) {
        // 使用String.format确保类型安全
        return String.format("%s:%d", prefix, id);
    }

    /**
     * 计算秒杀状态
     */
    private void calculateSeckillStatus(GoodsVo goods, Model model) {
        LocalDateTime startDate = goods.getStartDate();
        LocalDateTime endDate = goods.getEndDate();
        LocalDateTime nowDate = LocalDateTime.now();

        int seckillStatus;
        long remainSeconds;

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
    }
}