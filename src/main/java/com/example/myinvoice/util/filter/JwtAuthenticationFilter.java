//package com.example.myinvoice.util.filter;
//
//import com.example.myinvoice.Entity.User;
//import com.example.myinvoice.mapper.UserMapper;
//import com.example.myinvoice.util.login.JwtUtil;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final JwtUtil jwtUtil;
//    private final UserMapper userMapper;
//
//    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserMapper userMapper) {
//        this.jwtUtil = jwtUtil;
//        this.userMapper = userMapper;
//    }
//
//    @Override
//    protected void doFilterInternal(
//        HttpServletRequest request,
//        HttpServletResponse response,
//        FilterChain filterChain
//    ) throws ServletException, IOException {
//        try {
//            String path = request.getServletPath();
//            System.out.println("当前请求路径: " + path); // 打印请求路径
//
//            if (shouldNotFilter(request)) {
//
//                System.out.println("[DEBUG] 放行路径: " + path);
//                filterChain.doFilter(request, response); // 直接放行
//                return;
//            }
//            // 1. 从请求头获取 Token
//            String token = extractToken(request);
//            Long userId = jwtUtil.parseUserId(token);
//            System.out.println("[DEBUG] 提取的Token: " + token);
//            if (token == null) {
//                filterChain.doFilter(request, response);
//                return;
//            }
//
//            // 2. 验证并解析 Token
//            String username = jwtUtil.parseToken(token);
//
//            // 通过 ID 查询用户
//            User user = userMapper.selectById(userId);
//            if (user == null) {
//                throw new RuntimeException("用户不存在");
//            }
//
//            // 4. 转换角色为 GrantedAuthority（关键修复）
//            List<GrantedAuthority> authorities = new ArrayList<>();
//            if (user.getRole() != null) {
//                // 添加 ROLE_ 前缀并转为大写（与 Spring Security 约定一致）
//                String role = "ROLE_" + user.getRole().toUpperCase();
//                authorities.add(new SimpleGrantedAuthority(role));
//            }
//
//            // 5. 创建包含权限的 Authentication 对象
//            UsernamePasswordAuthenticationToken authentication =
//                    new UsernamePasswordAuthenticationToken(
//                            user.getUsername(),
//                            null,
//                            authorities  // 注入权限列表
//                    );
//
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            request.setAttribute("userId", user.getId());
//
//        } catch (Exception e) {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "认证失败: " + e.getMessage());
//            return;
//        }
//        filterChain.doFilter(request, response);
//    }
//    private String extractToken(HttpServletRequest request) {
//        String header = request.getHeader("Authorization");
//        if (header != null && header.startsWith("Bearer ")) {
//            return header.substring(7);
//        }
//        return null;
//    }
//
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) {
//        // 排除不需要认证的路径
//        String path = request.getServletPath();
//        return path.startsWith("/api/auth/login") ||
//                path.startsWith("/swagger-ui") ||     // Swagger UI页面
//                path.startsWith("/v3/api-docs") ||    // OpenAPI文档端点
//                path.startsWith("/swagger-resources") ||  // Swagger静态资源
//                path.startsWith("/webjars");          // Swagger依赖的WebJars
//    }
//
//}