package com.yx.seckill.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {IsMobileValidator.class}) // 校验逻辑类
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsMobile {

    boolean required() default true; // 是否必填

    String message() default "手机号码格式错误"; // 校验失败提示

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}