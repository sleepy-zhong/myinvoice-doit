package com.example.myinvoice.controller;

import com.example.myinvoice.Entity.DTO.LoginRequest;
import com.example.myinvoice.Entity.DTO.LoginResponse;
import com.example.myinvoice.mapper.UserMapper;
import com.example.myinvoice.server.login.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "登录权限接口")
public class AuthController {

    @Resource
    private UserMapper usersMapper;

    @Resource
    private AuthService authService;

    // 登录方法 2：使用 AuthService 处理登录
    @PostMapping("/auth_login")
    public LoginResponse loginWithAuthService(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
