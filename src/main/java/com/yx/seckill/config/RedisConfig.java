package com.yx.seckill.config;

// 新增导入

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // key序列化 → String
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // --- 这是修改的核心部分 ---
        // 1. 创建一个 ObjectMapper 对象
        ObjectMapper objectMapper = new ObjectMapper();
        // 2. 为 ObjectMapper 注册 JavaTimeModule 模块，让它支持 LocalDateTime 等类型
        objectMapper.registerModule(new JavaTimeModule());
        // 3. 使用配置好的 ObjectMapper 创建 JSON 序列化器
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        // --- 修改结束 ---

        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(jsonSerializer);

        // hash结构的序列化策略
        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(jsonSerializer);

        template.afterPropertiesSet();
        return template;
    }
}