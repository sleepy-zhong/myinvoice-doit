package com.example.myinvoice.server.Impl;

import com.aliyun.credentials.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myinvoice.Entity.DTO.RegisterRequest;
import com.example.myinvoice.Entity.DTO.UpdateProfileRequest;
import com.example.myinvoice.Entity.Ticket;
import com.example.myinvoice.Entity.User;
import com.example.myinvoice.mapper.TicketMapper;
import com.example.myinvoice.mapper.UserMapper;
import com.example.myinvoice.server.Invoice.UserService;
import com.example.myinvoice.util.DepartmentMappingConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DepartmentMappingConfig departmentMappingConfig;

    private PasswordEncoder passwordEncoder;  // 注入PasswordEncoder


    @Autowired
    private TicketMapper ticketMapper;


    public String generateEmployeeId(String departmentName) {
        try {
            // 1. 获取当前年份
            String year = String.valueOf(LocalDate.now().getYear());

            // 2. 部门编号映射
            Map<String, String> departmentMap = departmentMappingConfig.getDepartments();
            String deptCode = departmentMap.getOrDefault(departmentName, "00");
            if (departmentMap == null || departmentMap.isEmpty()) {
                throw new IllegalStateException("部门映射配置未加载");
            }

            if ("00".equals(deptCode)) {
                // 如果部门名称不在映射表中，抛出自定义异常或记录日志
                throw new IllegalArgumentException("未知的部门名称: " + departmentName);
            }

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
                if (!lastId.startsWith(prefix)) {
                    // 如果前缀不匹配，可能是数据异常，记录日志并忽略
                    System.err.println("警告：前缀不匹配的 employee_id 发现，忽略此条记录");
                } else {
                    String serialStr = lastId.substring(prefix.length());
                    serial = Integer.parseInt(serialStr) + 1;
                }
            }

            // 5. 格式化三位流水号
            String employeeId = prefix + String.format("%03d", serial); // 确保流水号始终为三位
            System.out.println("生成的 employee_id: " + employeeId); // 添加日志
            return employeeId;

        } catch (Exception e) {
            // 异常处理：记录日志并抛出自定义异常
            System.err.println("生成员工ID时发生错误: " + e.getMessage());
            throw new RuntimeException("生成员工ID失败", e);
        }
    }

    public void register(RegisterRequest request) {
        try {
            // 1. 判断手机号是否已注册
            User existingUserByPhone = userMapper.selectOne(
                    new QueryWrapper<User>().eq("phone", request.getPhone())
            );
            if (existingUserByPhone != null) {
                System.err.println("手机号已存在: " + request.getPhone()); // 添加日志
                throw new RuntimeException("该手机号已注册，请直接登录");
            }

            // 2. 自动生成工号
            String employeeId = generateEmployeeId(request.getDepartment());

            // 3. 加密密码
            String encodedPassword = new BCryptPasswordEncoder().encode(request.getPassword());

            // 4. 构建用户实体
            User user = new User();
            user.setUsername(request.getUsername()); // 可重复
            user.setPassword(encodedPassword);
            user.setPhone(request.getPhone()); // 必须唯一
            user.setDepartment(request.getDepartment());
            user.setRole(request.getRole());
            user.setEmployeeId(employeeId);
            user.setCreatedAt(LocalDateTime.now()); // 设置创建时间
            System.out.println(user);

            // 5. 插入数据库
            userMapper.insert(user);

        } catch (DuplicateKeyException e) {
            // 处理唯一键冲突
            System.err.println("用户名已存在: " + request.getUsername());
            throw new RuntimeException("该用户名已存在，请选择其他用户名", e);
        } catch (Exception e) {
            // 其他异常处理
            System.err.println("注册用户时发生错误: " + e.getMessage());
            throw new RuntimeException("注册用户失败", e);
        }
    }


    @Override
    public boolean deleteUserByEmployeeId(String employeeId) {
        if (employeeId == null || employeeId.trim().isEmpty()) {
            throw new IllegalArgumentException("工号不能为空");
        }

        try {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("employee_id", employeeId);
            int deletedCount = userMapper.delete(wrapper);

            if (deletedCount > 0) {
                System.out.println("成功删除用户，工号: " + employeeId);
                return true;
            } else {
                System.err.println("未找到对应工号的用户: " + employeeId);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace(); // 添加日志帮助调试
            throw new RuntimeException("删除用户失败", e);
        }
    }

    @Override
    public boolean updateProfile(String username, UpdateProfileRequest request) {
        // 查找用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) return false;

        // 手机号是否已存在，排除自己
        QueryWrapper<User> phoneCheckWrapper = new QueryWrapper<>();
        phoneCheckWrapper.eq("phone", request.getPhone()).ne("id", user.getId());
        User existingUser = userMapper.selectOne(phoneCheckWrapper);
        if (existingUser != null) {
            return false; // 手机号已存在
        }

        // 更新手机号和密码
        if (!StringUtils.isEmpty(request.getPhone())) {
            user.setPhone(request.getPhone());
        }
        if (!StringUtils.isEmpty(request.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getPassword())); // 密码加密
        }

        // 更新数据库
        userMapper.updateById(user);
        return true;
    }

    @Override
    public boolean AdmindeleteUserByEmployeeId(String employeeId) {
        if (employeeId == null || employeeId.trim().isEmpty()) {
            throw new IllegalArgumentException("工号不能为空");
        }

        // 查找用户
        QueryWrapper<User> userWrapper = new QueryWrapper<>();
        userWrapper.eq("employee_id", employeeId);
        User user = userMapper.selectOne(userWrapper);

        if (user == null) {
            System.err.println("未找到对应工号的用户: " + employeeId);
            return false;
        }

        // 查找并删除关联票据
        QueryWrapper<Ticket> ticketWrapper = new QueryWrapper<>();
        ticketWrapper.eq("user_id", user.getId());
        List<Ticket> tickets = ticketMapper.selectList(ticketWrapper);
        if (tickets != null && !tickets.isEmpty()) {
            ticketMapper.delete(ticketWrapper); // 删除关联的票据
        }

        // 删除用户
        userMapper.delete(userWrapper);
        System.out.println("成功删除用户，工号: " + employeeId);
        return true;
    }




}
