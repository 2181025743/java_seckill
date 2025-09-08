package com.yx.seckill.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yx.seckill.entity.User;
import com.yx.seckill.exception.GlobalException;
import com.yx.seckill.mapper.UserMapper;
import com.yx.seckill.service.IUserService;
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
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public RespBean doLogin(LoginVo loginVo,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();

        // ---------------------
        // 1️⃣ 参数校验
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(formPass)) {
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);

        }
        // 手机号格式校验
        if (!ValidatorUtil.isMobile(mobile)) {
            throw new GlobalException(RespBeanEnum.MOBILE_ERROR);
        }

        // ---------------------
        // 2️⃣ 查询数据库中的用户
        User user = userMapper.selectById(mobile);
        if (user == null) {
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }

        // ---------------------
        // 3️⃣ 验证密码
        // 数据库存储的是 二次加密之后的密码 = MD5(formPass + salt)
        String dbPass = user.getPassword();
        String saltDB = user.getSlat(); // 每个用户的盐（注册时随机生成存下的）
        // 对前端传过来的 formPass 再加一层用户私盐加密
        String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);

        if (!dbPass.equals(calcPass)) {
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);

        }
        // 登录校验通过后👇
        String ticket = UUIDUtil.uuid();                     // 1. 生成随机 Ticket

        request.getSession().setAttribute(ticket, user);     // 2. 把 (ticket → user) 写进 Session

        CookieUtil.setCookie(request,                       // 3. 把 ticket 写到浏览器 Cookie
                response,
                "userTicket",                  //   Cookie 名
                ticket);                       //   Cookie 值
        return RespBean.success();                           // 4. 返回成功
        // ---------------------
    }
}