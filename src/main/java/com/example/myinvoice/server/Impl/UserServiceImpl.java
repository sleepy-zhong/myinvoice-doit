package com.example.myinvoice.server.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myinvoice.Entity.DTO.RegisterRequest;
import com.example.myinvoice.Entity.User;
import com.example.myinvoice.mapper.UserMapper;
import com.example.myinvoice.server.Invoice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    public String generateEmployeeId(String departmentName) {
        // 1. 获取当前年份
        String year = String.valueOf(LocalDate.now().getYear());

        // 2. 部门编号映射
        Map<String, String> departmentMap = Map.of(
                "财务部", "01",
                "技术部", "02",
                "行政部", "03"
        );
        String deptCode = departmentMap.getOrDefault(departmentName, "00");

        // 3. 拼接前缀
        String prefix = year + deptCode;

        // 4. 查询当前前缀下的最大 employee_id
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.likeRight("employee_id", prefix);
        wrapper.orderByDesc("employee_id").last("limit 1");

        User lastUser = userMapper.selectOne(wrapper);

        int serial = 1;
        if (lastUser != null) {
            String lastId = lastUser.getEmployeeId();
            String serialStr = lastId.substring(prefix.length());
            serial = Integer.parseInt(serialStr) + 1;
        }

        // 5. 格式化三位流水号
//        return prefix + String.format("%03d", serial);
        return prefix + serial;

    }
    public void register(RegisterRequest request) {
        // ✅ 1. 判断手机号是否已注册
        User existingUser = userMapper.selectOne(
                new QueryWrapper<User>().eq("phone", request.getPhone())
        );
        if (existingUser != null) {
            throw new RuntimeException("该手机号已注册，请直接登录");
        }

        // ✅ 2. 自动生成工号
        String employeeId = generateEmployeeId(request.getDepartment());

        // ✅ 3. 加密密码
        String encodedPassword = new BCryptPasswordEncoder().encode(request.getPassword());

        // ✅ 4. 构建用户实体
        User user = new User();
        user.setUsername(request.getUsername()); // 可重复
        user.setPassword(encodedPassword);
        user.setPhone(request.getPhone()); // 必须唯一
        user.setDepartment(request.getDepartment());
        user.setRole(request.getRole());
        user.setEmployeeId(employeeId);

        // ✅ 5. 插入数据库
        userMapper.insert(user);
    }

}