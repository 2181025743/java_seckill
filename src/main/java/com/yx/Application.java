package com.yx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@MapperScan("com.yx.seckill.mapper")
@EnableRedisHttpSession // 2. 添加这个注解！
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}