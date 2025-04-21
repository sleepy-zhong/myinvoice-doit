package com.example.myinvoice.server.Invoice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.myinvoice.Entity.DTO.*;

import com.example.myinvoice.Entity.User;

public interface UserService extends IService<User> {
    UserResponse getUserById(Long id);

    String generateEmployeeId(String departmentName);
    void register(RegisterRequest request);
    boolean deleteUserByEmployeeId(String employeeId);

    // 普通用户更新方法（只能修改自己的用户名、密码、手机号）


    // 普通用户更新方法（只能修改自己的用户名、密码、手机号）
    boolean updateUserInfo(Long id, UpdateUserRequest request);

    boolean adminUpdateUser(AdminUpdateUserRequest request) ;

    boolean AdmindeleteUserByEmployeeId(String employeeId);
}
