package com.yx.seckill.validator;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yx.seckill.utils.ValidatorUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

    private boolean required = false;

    @Override
    public void initialize(IsMobile constraintAnnotation) {
        this.required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 非必填 + 空值 → 通过
        if (!required && StringUtils.isEmpty(value)) {
            return true;
        }
        // 必填 或 非空值 → 调用工具类做正则校验
        return ValidatorUtil.isMobile(value);
    }
}