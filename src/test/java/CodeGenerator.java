package com.yx.seckill;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * MyBatis-Plus 代码生成器
 */
public class CodeGenerator {
    public static void main(String[] args) {
        // 1. 数据库连接配置
        String url = "jdbc:mysql://localhost:3306/seckill?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "aA1472580@"; // 改成你本地的MySQL密码

        // 2. 快速构建代码生成器
        FastAutoGenerator.create(url, username, password)
                // 全局配置
                .globalConfig(builder -> {
                    builder.author("yx") // 作者名
                            .outputDir(System.getProperty("user.dir") + "/src/test/java") // 生成的Java代码路径
                            .dateType(DateType.TIME_PACK) // 使用 java.time
                            .commentDate("yyyy-MM-dd") // 注释日期格式
                            .disableOpenDir(); // 不要每次生成完自动打开目录
                })
                // 包配置
                .packageConfig(builder -> {
                    builder.parent("com.yx.seckill") // 父包路径
                            .moduleName("") // 模块名(如不需要可置空)
                            .entity("entity") // 实体类包
                            .service("service") // service接口包
                            .serviceImpl("service.impl") // service实现类包
                            .mapper("mapper") // mapper接口包
                            .controller("controller") // controller包
                            .xml("mapper.xml") // mapper.xml位置
                            .pathInfo(Collections.singletonMap(OutputFile.xml,
                                    System.getProperty("user.dir") + "/src/test/resources/mapper")); // XML文件路径
                })
                // 策略配置
                .strategyConfig(builder -> {
                    // 需要生成的表（可指定多张，以逗号分隔）
                    builder.addInclude("t_goods", "t_order", "t_seckill_goods", "t_seckill_order")
                            .addTablePrefix("t_"); // 去掉表名前缀 t_

                    // Entity 策略配置
                    builder.entityBuilder()
                            .enableLombok() // 使用Lombok注解
                            .enableTableFieldAnnotation(); // 字段加 @TableField 注解

                    // Service 策略配置
                    builder.serviceBuilder()
                            .formatServiceFileName("%sService") // 默认 IUserService
                            .formatServiceImplFileName("%sServiceImpl");

                    // Controller 策略配置
                    builder.controllerBuilder()
                            .enableRestStyle() // 使用 @RestController
                            .formatFileName("%sController");

                    // Mapper 策略配置
                    builder.mapperBuilder()
                            .formatMapperFileName("%sMapper")
                            .formatXmlFileName("%sMapper");
                })
                // 模板配置（默认用 Freemarker）
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}