package com.yx.seckill.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.yx.seckill.entity.User;
import com.yx.seckill.exception.GlobalException;
import com.yx.seckill.mapper.UserMapper;
import com.yx.seckill.service.UserService;
import com.yx.seckill.utils.CookieUtil;
import com.yx.seckill.utils.MD5Util;
import com.yx.seckill.utils.UUIDUtil;
import com.yx.seckill.utils.ValidatorUtil;
import com.yx.seckill.vo.LoginVo;
import com.yx.seckill.vo.RespBean;
import com.yx.seckill.vo.RespBeanEnum;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Service
@Slf4j // 新增 @Slf4j 注解
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public RespBean doLogin(LoginVo loginVo,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        log.info("====== 开始处理登录请求, 手机号: {} ======", mobile); // 新增日志

        // 1. 参数校验
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(formPass)) {
            log.warn("登录失败: 手机号或密码为空, mobile: {}", mobile); // 新增日志
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        if (!ValidatorUtil.isMobile(mobile)) {
            log.warn("登录失败: 手机号格式错误, mobile: {}", mobile); // 新增日志
            throw new GlobalException(RespBeanEnum.MOBILE_ERROR);
        }

        // 2. 查询数据库中的用户
        log.info("步骤1: 准备从数据库查询用户信息, mobile: {}", mobile); // 新增日志
        User user = userMapper.selectById(mobile);
        if (user == null) {
            log.warn("登录失败: 用户不存在, mobile: {}", mobile); // 新增日志
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        log.info("步骤1完成: 成功从数据库获取到用户, mobile: {}", mobile); // 新增日志

        // 3. 验证密码
        log.info("步骤2: 准备校验密码, mobile: {}", mobile); // 新增日志
        String dbPass = user.getPassword();
        String saltDB = user.getSlat();
        String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);

        if (!dbPass.equals(calcPass)) {
            log.warn("登录失败: 密码错误, mobile: {}", mobile); // 新增日志
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        log.info("步骤2完成: 密码校验通过, mobile: {}", mobile); // 新增日志

        // 4. 生成Ticket并存入Redis
        String ticket = UUIDUtil.uuid();
        log.info("步骤3: 生成ticket: {}, 准备将用户信息存入Redis...", ticket); // 新增日志

        // 关键步骤：执行Redis操作
        try {
            redisTemplate.opsForValue().set("user:" + ticket, user);
            log.info("步骤3完成: 成功将用户信息存入Redis, key=user:{}", ticket); // 新增日志
        } catch (Exception e) {
            // 如果这里发生异常，上面的GlobalExceptionHandler会捕获并记录详细堆栈
            log.error("!!!!!! 存入Redis时发生严重错误, mobile: {} !!!!!!", mobile, e); // 主动记录一次
            throw e; // 重新抛出，让全局处理器处理
        }

        // 5. 设置Cookie并返回成功响应
        CookieUtil.setCookie(request, response, "userTicket", ticket);
        log.info("====== 登录成功, mobile: {}, 返回ticket: {} ======", mobile, ticket); // 新增日志
        return RespBean.success(ticket);
    }

    public User getUserByCookie(String userTicket,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        if (StringUtils.isEmpty(userTicket)) {
            return null;
        }
        Object obj = redisTemplate.opsForValue().get("user:" + userTicket);
        User user = null;
        if (obj != null) {
            if (obj instanceof User) {
                user = (User) obj;
            } else if (obj instanceof LinkedHashMap) {
                // 创建 ObjectMapper 并注册 JavaTimeModule
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule()); // ⬅️ 添加这一行！
                user = mapper.convertValue(obj, User.class);
            }
        }

        if (user != null) {
            CookieUtil.setCookie(request, response, "userTicket", userTicket);
        }
        return user;
    }
}