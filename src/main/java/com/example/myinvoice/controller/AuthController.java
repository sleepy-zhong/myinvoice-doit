package com.example.myinvoice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.myinvoice.Entity.DTO.LoginDTO;
import com.example.myinvoice.Entity.DTO.LoginRequest;
import com.example.myinvoice.Entity.DTO.LoginResponse;
import com.example.myinvoice.Entity.User;
import com.example.myinvoice.mapper.UserMapper;
import com.example.myinvoice.server.login.AuthService;
import com.example.myinvoice.util.login.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "登录权限接口")
public class AuthController {

    @Resource
    private UserMapper usersMapper;

    // 登录方法 1：使用用户名和密码直接登录
    @PostMapping("/login")
    public Result loginWithUsernameAndPassword(@RequestBody LoginDTO dto) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("employee_id", dto.getEmployeeId());

        User user = usersMapper.selectOne(wrapper);
        if (user == null) {
            return Result.error("用户不存在");
        }

        if (!user.getPassword().equals(dto.getPassword())) {
            return Result.error("密码错误");
        }

        // 登录成功后返回用户信息（或生成 token）
        return Result.success(user);
    }

    @Resource
    private AuthService authService;

    // 登录方法 2：使用 AuthService 处理登录
    @PostMapping("/auth_login")
    public LoginResponse loginWithAuthService(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
