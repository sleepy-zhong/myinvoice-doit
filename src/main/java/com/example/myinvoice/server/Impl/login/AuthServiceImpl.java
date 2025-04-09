package com.example.myinvoice.server.Impl.login;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.myinvoice.Entity.DTO.LoginRequest;
import com.example.myinvoice.Entity.DTO.LoginResponse;
import com.example.myinvoice.Entity.User;
import com.example.myinvoice.mapper.UserMapper;
import com.example.myinvoice.server.login.AuthService;
import com.example.myinvoice.util.login.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest request) {
        // 使用 MyBatis-Plus 查询用户
        User user = userMapper.selectOne(
                new QueryWrapper<User>().eq("employee_id", request.getEmployeeId())
        );

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证密码（使用 BCrypt）
        if (!new BCryptPasswordEncoder().matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 登录成功生成 Token
        String token = null;
        try {
            token = jwtUtil.createToken(user);  // 调用 createToken 方法生成 token
        } catch (Exception e) {
            throw new RuntimeException("生成 Token 失败", e);
        }

        return new LoginResponse(token, user.getRole());
    }
}
