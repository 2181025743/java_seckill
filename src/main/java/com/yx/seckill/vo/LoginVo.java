package com.yx.seckill.vo;

import com.yx.seckill.validator.IsMobile;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class LoginVo {

    @NotNull(message = "手机号码不能为空")
    @IsMobile  // ⬅️ 我们自定义的手机号格式注解
    private String mobile;

    @NotNull(message = "密码不能为空")
    @Length(min = 32, message = "密码加密后长度应为32位")
    private String password;
}