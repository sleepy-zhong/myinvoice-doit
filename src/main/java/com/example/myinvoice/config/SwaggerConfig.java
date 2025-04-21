//package com.example.myinvoice.config;
//
//import io.swagger.v3.oas.models.Components;
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Info;
//import io.swagger.v3.oas.models.security.SecurityRequirement;
//import io.swagger.v3.oas.models.security.SecurityScheme;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class SwaggerConfig {
//    @Bean
//    public OpenAPI customOpenAPI() {
//        // 定义安全方案（Bearer Token）
//        SecurityScheme securityScheme = new SecurityScheme()
//                .type(SecurityScheme.Type.HTTP)
//                .scheme("bearer")
//                .bearerFormat("JWT");
//
//        // 将安全方案添加到全局组件中
//        Components components = new Components()
//                .addSecuritySchemes("bearerAuth", securityScheme);
//
//        // 设置全局安全需求（所有接口默认需要认证）
//        SecurityRequirement securityRequirement = new SecurityRequirement()
//                .addList("bearerAuth");
//
//        return new OpenAPI()
//                .components(components)
//                .info(new Info()
//                        .title("发票管理系统接口文档")
//                        .version("1.0.0")
//                        .description("这是一个票据审核、上传和查询的后端接口文档"))
//                .addSecurityItem(securityRequirement);
//    }
//}