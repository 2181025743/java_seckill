package com.yx.seckill.exception;

import com.yx.seckill.vo.RespBean;
import com.yx.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j // 新增 @Slf4j 注解
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public RespBean exceptionHandler(Exception e) {
        // 1. 如果是自定义的 GlobalException
        if (e instanceof GlobalException ex) {
            // 修改：记录警告级别的日志
            log.warn("捕获到业务异常(GlobalException): {}", ex.getRespBeanEnum().getMessage());
            return RespBean.error(ex.getRespBeanEnum());
        }
        // 2. 如果是参数校验的 BindException
        else if (e instanceof BindException ex) {
            RespBean respBean = RespBean.error(RespBeanEnum.BIND_ERROR);
            String errorMsg = "参数校验异常：" + ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
            respBean.setMessage(errorMsg);
            // 修改：记录警告级别的日志
            log.warn("捕获到参数校验异常(BindException): {}", errorMsg);
            return respBean;
        }
        // 3. 其他未知异常
        // 修改：记录错误级别的日志，并打印完整的异常堆栈信息(e)
        // log.error("捕获到未知的系统异常(Exception):", e);
        return RespBean.error(RespBeanEnum.ERROR);
    }
}