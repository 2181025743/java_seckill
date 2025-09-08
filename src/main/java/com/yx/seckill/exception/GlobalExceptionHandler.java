package com.yx.seckill.exception;

import com.yx.seckill.vo.RespBean;
import com.yx.seckill.vo.RespBeanEnum;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public RespBean exceptionHandler(Exception e) {
        // 1. 如果是自定义的 GlobalException
        if (e instanceof GlobalException ex) {
            return RespBean.error(ex.getRespBeanEnum());
        }
        // 2. 如果是参数校验的 BindException
        else if (e instanceof BindException ex) {
            RespBean respBean = RespBean.error(RespBeanEnum.BIND_ERROR);
            respBean.setMessage("参数校验异常：" +
                    ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
            return respBean;
        }
        // 3. 其他未知异常
        return RespBean.error(RespBeanEnum.ERROR);
    }
}
