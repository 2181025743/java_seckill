package com.yx.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yx.seckill.entity.User;
import com.yx.seckill.vo.LoginVo;
import com.yx.seckill.vo.RespBean;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 杨潇
 * @since 2025-09-06
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录方法
     *
     * @param loginVo  登录参数
     * @param request  请求对象
     * @param response 响应对象
     * @return 登录结果
     */
    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    User getUserByCookie(String userTicket,
                         HttpServletRequest request,
                         HttpServletResponse response);
}
