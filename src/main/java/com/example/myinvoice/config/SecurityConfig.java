package com.example.myinvoice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 配置哪些请求可以不经过身份认证
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll() // 允许公开访问 Swagger UI 和 API 文档
                        .anyRequest().authenticated() // 其他请求需要身份认证
                )
                .formLogin(withDefaults()) // 使用默认的表单登录
                .logout(withDefaults()); // 使用默认的注销配置

        return http.build(); // 返回配置完成的 SecurityFilterChain
    }
}
