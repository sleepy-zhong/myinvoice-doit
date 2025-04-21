package com.example.myinvoice.config;


//import com.example.myinvoice.util.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//@Configuration
//public class SecurityConfig {
//
//    private final JwtAuthenticationFilter jwtAuthenticationFilter;
//    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
//        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                // 禁用 CSRF（前后端分离项目建议关闭）
//                .csrf(csrf -> csrf.disable())
//
//
//                // 配置请求授权规则
//                .authorizeHttpRequests(authz -> authz
//                         //公开访问路径
//                        .requestMatchers(
//                                "/api/auth/login",
//                                "/swagger-ui/**",
//                                "/v3/api-docs/**",
//                                "/v2/api-docs",
//                                "/swagger-resources/**",
//                                "/webjars/**",
//                                "/doc.html","/webjars/**", "/swagger-resources", "/swagger-resources/**", "/v3/**", "/favicon.ico", "Mozilla/**"
//                                ,"/api/**"
//                        ).permitAll()
//                        // 其他请求需要认证
//                        .anyRequest().authenticated()
//                )
//
//                // 添加 JWT 过滤器
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//
//                // 设置无状态会话（JWT 不需要 Session）
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                );
//
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().permitAll() // 所有请求都允许访问
                )
                .csrf(csrf -> csrf.disable()); // 如果是前后端分离项目，建议关闭 CSRF

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder() ;
    }
}