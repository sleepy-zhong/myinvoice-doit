package com.example.myinvoice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("发票管理系统接口文档")
                        .version("1.0.0")
                        .description("这是一个票据审核、上传和查询的后端接口文档"));
    }
}
